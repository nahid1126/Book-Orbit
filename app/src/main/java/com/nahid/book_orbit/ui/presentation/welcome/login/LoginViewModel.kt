package com.nahid.book_orbit.ui.presentation.welcome.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.domain.repository.AuthRepository
import com.nahid.book_orbit.core.utils.extension.getErrorMessage
import com.nahid.book_orbit.data.local.AppPreference
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"
class LoginViewModel(
    private val authRepository: AuthRepository,
    private val appPreference: AppPreference
) :
    ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState())
        private set

    var passwordVisibility by mutableStateOf(false)
        private set

    fun updateUiState(uiState: LoginUiState) {
        this.uiState = uiState

    }

    fun updatePasswordVisibility(visibility: Boolean) {
        passwordVisibility = visibility
    }


    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = authRepository.signInWithGoogle(idToken)
            uiState = when (result) {
                is Results.Error -> {
                    uiState.copy(
                        isLoading = false,
                        message = Pair(false, result.exception.getErrorMessage())
                    )
                }

                is Results.Success -> {
                    if (result.data != null) {
                        appPreference.saveToken(result.data.uid)
                        appPreference.storeUserGmail(result.data.email)
                        Log.d(TAG, "googleLogin: ${result.data.email} ${result.data.providerId}, ${result.data.uid}")
                    }
                    uiState.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                        userInfo = result.data,
                        message = Pair(true, "Login Success")
                    )
                }
            }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val userName: String = "admin",
    val password: String = "r@321!",
    val androidId: String? = "123456789",
    val appVersion: String? = "4.5.0",
    val message: Pair<Boolean, String>? = null,
    val isLoginSuccess: Boolean = false,
    val userInfo: FirebaseUser? = null
)