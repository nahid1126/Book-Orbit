package com.nahid.book_orbit.ui.presentation.home

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())

    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {

        }
    }

    fun updateUiState(uiState: HomeScreenUiState) {
        _uiState.value = uiState
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



}

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val exception: Exception? = null,
    val showExitDialog: Boolean = false,
)