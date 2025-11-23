package com.nahid.book_orbit.ui.presentation.gams

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.local.AppPreference
import com.nahid.book_orbit.data.remote.dto.Gems
import com.nahid.book_orbit.data.remote.dto.GemsTransaction
import com.nahid.book_orbit.domain.repository.GemsRepository
import kotlinx.coroutines.launch

private const val TAG = "GemsViewModel"

class GemsViewModel(
    private val appPreference: AppPreference,
    private val gemsRepository: GemsRepository
) : ViewModel() {
    var uiState: GemsUiState by mutableStateOf(GemsUiState())
        private set

    fun updateUiState(uiState: GemsUiState) {
        this.uiState = uiState
    }

    init {
        getAllGems()
    }

    fun putGems() {
        viewModelScope.launch {
            Log.d(TAG, "putGems: ${uiState.totalGems}")
            appPreference.storeTotalGems(uiState.totalGems)
        }
    }
    private fun getAllGems() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val gems = gemsRepository.getAllGems()
                uiState = when (gems) {
                    is Results.Error -> {
                        uiState.copy(
                            isLoading = false,
                            message =  gems.exception.message.toString()
                        )
                    }

                    is Results.Success -> {
                        uiState.copy(isLoading = false, gemsList = gems.data)
                    }
                }
            } catch (e: Exception) {
                uiState =
                    uiState.copy(isLoading = false, message =  e.message.toString())
            }
        }
    }
    fun getGems() {
        viewModelScope.launch {
            if (uiState.uId.isNullOrEmpty()) {
                uiState.copy(message = "User Name is Empty")
            } else {
                val response = gemsRepository.getTotalGems(uiState.uId ?: "")
                uiState = when (response) {
                    is Results.Error -> {
                        uiState.copy(
                            isLoading = false,
                            message = response.exception.message
                        )
                    }

                    is Results.Success -> {
                        uiState.copy(
                            isLoading = false,
                            totalGems = response.data,
                            isGemsPurchase = true
                        )
                    }
                }
            }
        }
    }

    fun buyGems() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            if (uiState.uId.isNullOrEmpty()) {
                uiState =
                    uiState.copy(isLoading = false, message =  "User Id Not Found")
            } else if (uiState.gemsId.isNullOrEmpty()) {
                uiState =
                    uiState.copy(isLoading = false, message =  "Gems Id Not Found")
            } else {
                val response = gemsRepository.purchaseGems(uiState.uId ?: "", uiState.gemsId ?: "")
                uiState = when (response) {
                    is Results.Error -> {
                        uiState.copy(
                            isLoading = false,
                            message =  response.exception.message.toString()
                        )
                    }

                    is Results.Success -> {
                       getGems()
                        uiState.copy(
                            isLoading = false,
                            message = "Gems Purchased Successfully"
                        )
                    }
                }
            }
        }
    }
    fun getHistory() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            if (uiState.uId.isNullOrEmpty()) {
                uiState =
                    uiState.copy(isLoading = false, message =  "User Id Not Found")
            } else {
                val response = gemsRepository.getTransactionHistory(uiState.uId ?: "")
                uiState = when (response) {
                    is Results.Error -> {
                        uiState.copy(
                            isLoading = false,
                            message =  response.exception.message.toString()
                        )
                    }

                    is Results.Success -> {
                        uiState.copy(
                            isLoading = false,
                            transactionHistory = response.data
                        )
                    }
                }
            }
        }
    }
}

data class GemsUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val showExitDialog: Boolean = false,
    val gemsList: List<Gems>? = emptyList(),
    val uId: String? = null,
    val gemsId: String? = null,
    val showConfirmationDialog: Boolean = false,
    val transactionHistory: List<GemsTransaction>? = emptyList(),
    val isGemsPurchase: Boolean = false,
    val totalGems: Long? = 0L
)