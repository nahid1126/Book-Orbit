package com.nahid.book_orbit.ui.presentation.gams

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Gems
import com.nahid.book_orbit.domain.repository.GemsRepository
import kotlinx.coroutines.launch

class GemsViewModel(private val gemsRepository: GemsRepository) : ViewModel() {
    var uiState: GemsUiState by mutableStateOf(GemsUiState())
        private set

    fun updateUiState(uiState: GemsUiState) {
        this.uiState = uiState
    }

    init {
        getAllGems()
    }
   private fun getAllGems() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val gems = gemsRepository.getAllGems()
                uiState = when (gems) {
                    is Results.Error -> {
                        uiState.copy(isLoading = false, exception = gems.exception)
                    }

                    is Results.Success -> {
                        uiState.copy(isLoading = false, gemsList = gems.data)
                    }
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, exception = e)
            }
        }
    }
}

data class GemsUiState(
    val isLoading: Boolean = false,
    val message: Pair<Boolean, String>? = null,
    val exception: Exception? = null,
    val showExitDialog: Boolean = false,
    val gemsList: List<Gems>? = emptyList()
)