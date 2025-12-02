package com.nahid.book_orbit.domain.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.nahid.book_orbit.core.utils.PurchaseResult
import com.nahid.book_orbit.core.utils.Results
import kotlinx.coroutines.flow.Flow

interface BillingRepository {

    suspend fun getAvailableProducts(productIds: List<String>): Results<List<ProductDetails>>

    fun purchase(
        productDetails: ProductDetails,
        activity: Activity
    ): Flow<PurchaseResult>

    suspend fun restorePurchases(): Results<List<Purchase>>
    suspend fun processRestoredPurchase(purchase: Purchase)
}