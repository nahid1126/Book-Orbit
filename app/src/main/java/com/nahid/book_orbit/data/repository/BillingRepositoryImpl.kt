package com.nahid.book_orbit.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
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
import com.nahid.book_orbit.core.utils.PurchaseResult
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.domain.repository.BillingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
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
        MutableSharedFlow<Pair<BillingResult, List<Purchase>?>>(replay = 1)

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
                connectBillingClient()
            }
        })
    }

    // ------------------ PRODUCTS ------------------
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

    // ------------------ PURCHASE FLOW ------------------
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
                            launch { processPurchase(purchase, this@callbackFlow) }
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
        Log.d(TAG, "launchBillingFlow result: $result")
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            trySend(PurchaseResult.Error("Failed to launch billing flow: ${result.debugMessage}"))
            close()
        }

        awaitClose { job.cancel() }
    }

    // ------------------ PROCESS PURCHASE ------------------
    private suspend fun processPurchase(
        purchase: Purchase,
        channel: ProducerScope<PurchaseResult>
    ) {
        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {

                // 1️⃣ Acknowledge
                if (!purchase.isAcknowledged) {
                    val ackResult = acknowledgePurchase(purchase)
                    if (!ackResult) {
                        channel.trySend(PurchaseResult.Error("Acknowledge failed"))
                        channel.close()
                        return
                    }
                }

                // 2️⃣ Optional: Grant gems / coins in Firestore
                val granted = grantPurchase(purchase)
                if (!granted) {
                    channel.trySend(PurchaseResult.Error("Grant failed"))
                    channel.close()
                    return
                }

                // 3️⃣ Consume purchase
                val consumed = consumePurchase(purchase)
                channel.trySend(
                    if (consumed) PurchaseResult.Consumed else PurchaseResult.Error("Consume failed")
                )
                channel.close()
            }

            Purchase.PurchaseState.PENDING -> {
                channel.trySend(PurchaseResult.Pending)
            }
        }
    }

    private suspend fun acknowledgePurchase(purchase: Purchase): Boolean =
        suspendCoroutine { cont ->
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(params) { result ->
                cont.resume(result.responseCode == BillingClient.BillingResponseCode.OK)
            }
        }

    private suspend fun grantPurchase(purchase: Purchase): Boolean = withContext(Dispatchers.IO) {
        try {
            // Optional: skip if you don't need Firestore
            /*
            val user = firebaseAuth.currentUser ?: return@withContext false
            val userDoc = firestore.collection("wallets").document(user.uid)
            val gems = ... // map productId to gems
            firestore.runTransaction { tx ->
                val snapshot = tx.get(userDoc)
                val current = snapshot.getLong("gems") ?: 0L
                tx.set(userDoc, mapOf("gems" to (current + gems)), SetOptions.merge())
            }.await()
            */
            true
        } catch (e: Exception) {
            Log.e(TAG, "Grant failed", e)
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

    // ------------------ RESTORE PURCHASES ------------------
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
    override suspend fun processRestoredPurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            // Acknowledge
            acknowledgePurchase(purchase)
            // Optional: Grant gems
            grantPurchase(purchase)
            // Consume
            consumePurchase(purchase)
        }
    }
}

