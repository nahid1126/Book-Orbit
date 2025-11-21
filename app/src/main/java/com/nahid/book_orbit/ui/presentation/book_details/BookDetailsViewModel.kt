package com.nahid.book_orbit.ui.presentation.book_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nahid.book_orbit.ui.presentation.home.HomeScreenUiState

class BookDetailsViewModel() : ViewModel() {
    var uiState: BookDetailsUi by mutableStateOf(BookDetailsUi())
        private set

    fun updateUiState(uiState: BookDetailsUi) {
        this.uiState = uiState
    }
}

data class BookDetailsUi(
    val isLoading: Boolean = false,
    val message: Pair<Boolean, String>? = null,
    val isBuyClicked: Boolean = false
)