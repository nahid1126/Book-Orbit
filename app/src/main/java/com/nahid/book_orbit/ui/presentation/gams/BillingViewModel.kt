package com.nahid.book_orbit.ui.presentation.gams

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.nahid.book_orbit.core.utils.PurchaseResult
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.domain.repository.BillingRepository
import kotlinx.coroutines.launch

class BillingViewModel(
    private val billingRepository: BillingRepository
) : ViewModel() {

    var uiState by mutableStateOf(BillingUiState())
        private set

    init {
        loadProducts()
        restorePendingPurchases()
    }

    fun updateSelectedProduct(product: ProductDetails) {
        uiState = uiState.copy(selectedProduct = product)
    }

    // -------------------------------------------------------------
    // LOAD PRODUCTS
    // -------------------------------------------------------------
    private fun loadProducts() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val ids = listOf(
                "nahid.book_orbit.gems.1",
                "nahid.book_orbit.gems.2",
                "nahid.book_orbit.gems.3",
                "nahid.book_orbit.gems.4",
                "nahid.book_orbit.gems.5",
                "nahid.book_orbit.gems.6",
                "nahid.book_orbit.gems.7",
                "nahid.book_orbit.gems.8"
            )

            uiState = when (val result = billingRepository.getAvailableProducts(ids)) {
                is Results.Success -> {
                    uiState.copy(
                        isLoading = false,
                        productList = result.data
                    )
                }

                is Results.Error -> {
                    uiState.copy(
                        isLoading = false,
                        message = result.exception.message
                    )
                }
            }
        }
    }

    // -------------------------------------------------------------
    // PURCHASE FLOW
    // -------------------------------------------------------------
    fun purchase(activity: Activity) {
        val product = uiState.selectedProduct ?: return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            billingRepository.purchase(product, activity)
                .collect { result ->

                    uiState = uiState.copy(
                        isLoading = false,
                        purchaseResult = result
                    )
                }
        }
    }

    // -------------------------------------------------------------
    // RESTORE UNCONSUMED PURCHASES
    // -------------------------------------------------------------
    private fun restorePendingPurchases() {
        viewModelScope.launch {
            when (val result = billingRepository.restorePurchases()) {
                is Results.Success -> {
                    result.data.forEach { purchase ->
                        billingRepository.consumeAndGrantPurchase(purchase)
                    }
                }
                is Results.Error -> {
                    // ignore or show message
                }
            }
        }
    }
}


data class BillingUiState(
    val isLoading: Boolean = false,
    val message: String? = null,

    val productList: List<ProductDetails> = emptyList(),

    val purchaseResult: PurchaseResult? = null,

    val selectedProduct: ProductDetails? = null
)