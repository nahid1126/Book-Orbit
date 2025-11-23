package com.nahid.book_orbit.ui.presentation.book_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.local.AppPreference
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.domain.repository.BookRepository
import kotlinx.coroutines.launch

private const val TAG = "BookDetailsViewModel"

class BookDetailsViewModel(
    private val appPreference: AppPreference,
    private val bookRepository: BookRepository
) : ViewModel() {
    var uiState: BookDetailsUi by mutableStateOf(BookDetailsUi())
        private set

    fun updateUiState(uiState: BookDetailsUi) {
        Log.d(TAG, "updateUiState: call")
        this.uiState = uiState
    }
    fun putGems() {
        viewModelScope.launch {
            appPreference.storeTotalGems(uiState.totalGems)
        }
    }

    fun purchasesBook() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            if (uiState.uId.isNullOrEmpty()) {
                uiState =
                    uiState.copy(isLoading = false, message = "User Id Not Found")
            } else uiState.currentGems?.let {
                if ((it < uiState.book!!.price) || (uiState.currentGems == 0L)) {
                    uiState = uiState.copy(isLoading = false, message = "Not Enough Gems")
                } else {
                    val data = hashMapOf(
                        "userId" to uiState.uId,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "books.${uiState.book?.id}" to hashMapOf(
                            "purchased" to true,
                            "price" to (uiState.book?.price?.toLong() ?: 0L)
                        ),
                    )

                    val response = bookRepository.purchasedBook(uiState.uId, data)
                    uiState = when (response) {
                        is Results.Error -> {
                            uiState.copy(
                                isLoading = false,
                                message = response.exception.message.toString()
                            )
                        }

                        is Results.Success -> {
                            reduceGems()
                            uiState.copy(
                                isLoading = false,
                                message = "Book Purchased Successfully",
                                isBookPurchased = true
                            )
                        }
                    }
                }
            }
        }
    }

   private fun reduceGems() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            if (uiState.uId.isNullOrEmpty()) {
                uiState =
                    uiState.copy(isLoading = false, message = "User Id Not Found")
            } else if (uiState.book == null) {
                uiState = uiState.copy(isLoading = false, message = "Book Not Found")
            } else {
                val response = bookRepository.deductGems(uiState.uId ?: "", uiState.book!!.price)
                uiState = when (response) {
                    is Results.Error -> {
                        uiState.copy(
                            isLoading = false,
                            message = response.exception.message.toString()
                        )
                    }
                    is Results.Success -> {
                        getGems()
                        uiState.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getGems() {
        viewModelScope.launch {
            if (uiState.uId.isNullOrEmpty()) {
                uiState.copy(message = "User Name is Empty")
            } else {
                val response = bookRepository.getTotalGems(uiState.uId ?: "")
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
                        )
                    }
                }
            }
        }
    }

    fun getPurchaseBooks() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            if (uiState.uId.isNullOrEmpty()) {
                uiState =
                    uiState.copy(isLoading = false, message = "User Id Not Found")
            } else {
                val response = bookRepository.getPurchasedBooks(uiState.uId ?: "")
                uiState = when (response) {
                    is Results.Error -> {
                        uiState.copy(
                            isLoading = false,
                            message = response.exception.message.toString()
                        )
                    }

                    is Results.Success -> {
                        uiState.copy(
                            isLoading = false,
                            purchaseBooks = response.data
                        )
                    }
                }
            }
        }
    }
}

data class BookDetailsUi(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isBuyClicked: Boolean = false,
    val uId: String? = null,
    val book: Book? = null,
    val purchaseBooks: List<Book>? = emptyList(),
    val currentGems: Long? = 0L,
    val isBookPurchased: Boolean = false,
    val totalGems: Long? = 0L

)