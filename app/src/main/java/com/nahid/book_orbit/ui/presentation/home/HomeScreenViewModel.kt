package com.nahid.book_orbit.ui.presentation.home

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.domain.repository.BookRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val bookRepository: BookRepository) : ViewModel() {
    var uiState: HomeScreenUiState by mutableStateOf(HomeScreenUiState())
        private set


    init {
        viewModelScope.launch {

        }
    }

    fun updateUiState(uiState: HomeScreenUiState) {
        this.uiState = uiState
    }


    @SuppressLint("DefaultLocale")
    fun calculateProgress(response: Int, target: Int): Int {
        var percent = if (response == 0) {
            0
        } else if (target == 0) {
            0
        } else {
            (response.toFloat() / target.toFloat()) * 100
        }
        return percent.toInt()
    }

    fun getAllBooks() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val books = bookRepository.getAllBooks()
                uiState = when (books) {
                    is Results.Error -> {
                        uiState.copy(isLoading = false, exception = books.exception)
                    }

                    is Results.Success -> {
                        uiState.copy(isLoading = false, books = books.data)
                    }
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, exception = e)
            }
        }
    }



}

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val exception: Exception? = null,
    val showExitDialog: Boolean = false,
    val books: List<Book> = emptyList(),
)