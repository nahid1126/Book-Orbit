package com.nahid.book_orbit.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.consumePurchase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nahid.book_orbit.core.utils.PurchaseResult
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.domain.repository.BillingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "BillingRepositoryImpl"

class BillingRepositoryImpl(
    private val context: Context,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : BillingRepository {

    private val billingClient: BillingClient
    private val purchaseUpdatesFlow =
        MutableSharedFlow<Pair<BillingResult, List<Purchase>?>>(replay = 0)

    init {
        val listener = PurchasesUpdatedListener { billingResult, purchases ->
            purchaseUpdatesFlow.tryEmit(billingResult to purchases)
        }

        billingClient = BillingClient.newBuilder(context)
            .setListener(listener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .enableAutoServiceReconnection()
            .build()

        connectBillingClient()
    }

    private fun connectBillingClient() {
        if (billingClient.isReady) return

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    Log.e(TAG, "Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                // Retry
                connectBillingClient()
            }
        })
    }

    // -------------------------------------------------------------
    // PRODUCTS
    // -------------------------------------------------------------
    override suspend fun getAvailableProducts(productIds: List<String>): Results<List<ProductDetails>> =
        suspendCoroutine { cont ->

            if (!billingClient.isReady) {
                cont.resume(Results.Error(Exception("BillingClient not ready")))
                return@suspendCoroutine
            }

            val queryList = productIds.map {
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(queryList)
                .build()

            billingClient.queryProductDetailsAsync(params) { result, productList ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    cont.resume(Results.Success(productList.productDetailsList))
                } else {
                    cont.resume(Results.Error(Exception(result.debugMessage)))
                }
            }
        }

    // -------------------------------------------------------------
    // PURCHASE FLOW
    // -------------------------------------------------------------
    override fun purchase(
        productDetails: ProductDetails,
        activity: Activity
    ): Flow<PurchaseResult> = callbackFlow {

        if (!billingClient.isReady) {
            trySend(PurchaseResult.Error("Billing client not ready"))
            close()
            return@callbackFlow
        }

        val job = launch {
            purchaseUpdatesFlow.collect { (billingResult, purchases) ->

                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        if (!purchases.isNullOrEmpty()) {
                            val purchase = purchases.first()
                            processPurchase(purchase, this@callbackFlow)
                        } else {
                            trySend(PurchaseResult.Error("Purchase OK but no data"))
                            close()
                        }
                    }

                    BillingClient.BillingResponseCode.USER_CANCELED -> {
                        trySend(PurchaseResult.Cancelled)
                        close()
                    }

                    else -> {
                        trySend(PurchaseResult.Error("Billing Error: ${billingResult.debugMessage}"))
                        close()
                    }
                }
            }
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            ).build()

        val result = billingClient.launchBillingFlow(activity, flowParams)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            trySend(PurchaseResult.Error("Failed to launch billing flow: ${result.debugMessage}"))
            close()
        }

        awaitClose { job.cancel() }
    }

    private suspend fun processPurchase(
        purchase: Purchase,
        channel: kotlinx.coroutines.channels.ProducerScope<PurchaseResult>
    ) {
        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                val granted = verifyAndGrant(purchase)
                if (granted) {
                    val consumed = consumePurchase(purchase)
                    channel.trySend(
                        if (consumed) PurchaseResult.Consumed else PurchaseResult.Error(
                            "Verified but failed to consume"
                        )
                    )
                } else {
                    channel.trySend(PurchaseResult.Error("Verification failed"))
                }
            }

            Purchase.PurchaseState.PENDING -> channel.trySend(PurchaseResult.Pending)
        }
        channel.close()
    }

    // -------------------------------------------------------------
    // RESTORE PURCHASES
    // -------------------------------------------------------------
    override suspend fun restorePurchases(): Results<List<Purchase>> =
        suspendCancellableCoroutine { cont ->
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            billingClient.queryPurchasesAsync(params) { result, purchases ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    cont.resume(Results.Success(purchases))
                } else {
                    cont.resume(Results.Error(Exception(result.debugMessage)))
                }
            }
        }

    // -------------------------------------------------------------
    // CONSUME + GRANT GEMS
    // -------------------------------------------------------------
    override suspend fun consumeAndGrantPurchase(purchase: Purchase): Results<Boolean> =
        try {
            val granted = verifyAndGrant(purchase)
            if (granted) {
                val consumed = consumePurchase(purchase)
                Results.Success(consumed)
            } else Results.Error(Exception("Verification failed"))
        } catch (e: Exception) {
            Results.Error(e)
        }

    private suspend fun verifyAndGrant(purchase: Purchase): Boolean = withContext(Dispatchers.IO) {
        try {
            val user = firebaseAuth.currentUser ?: return@withContext false
            val userDoc = firestore.collection("wallets").document(user.uid)

            val gems = purchase.products.firstOrNull()?.let { id ->
                when (id) {
                    "nahid.book_orbit.gems.1" -> 200
                    "nahid.book_orbit.gems.2" -> 420
                    "nahid.book_orbit.gems.3" -> 650
                    "nahid.book_orbit.gems.4" -> 1100
                    "nahid.book_orbit.gems.5" -> 1600
                    "nahid.book_orbit.gems.6" -> 2400
                    "nahid.book_orbit.gems.7" -> 3600
                    "nahid.book_orbit.gems.8" -> 5200
                    else -> 0
                }
            } ?: 0

            if (gems == 0) return@withContext false

            firestore.runTransaction { tx ->
                val snapshot = tx.get(userDoc)
                val current = snapshot.getLong("gems") ?: 0L
                tx.set(userDoc, mapOf("gems" to (current + gems)), SetOptions.merge())
                null
            }.await()

            true
        } catch (e: Exception) {
            Log.e(TAG, "Granting gems failed", e)
            false
        }
    }

    private suspend fun consumePurchase(purchase: Purchase): Boolean = withContext(Dispatchers.IO) {
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val result = billingClient.consumePurchase(params)
        result.billingResult.responseCode == BillingClient.BillingResponseCode.OK
    }

    companion object {
        private const val TAG = "BillingRepositoryImpl"
    }
}
