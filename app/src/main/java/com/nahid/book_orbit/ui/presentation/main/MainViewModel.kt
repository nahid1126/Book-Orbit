package com.nahid.book_orbit.ui.presentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.book_orbit.data.local.AppPreference
import com.nahid.book_orbit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel(
    private val appPreference: AppPreference,
    private val loginRepository: AuthRepository,
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(MainUiState())

    var uiState = mutableUiState.asStateFlow()
    // val loginResponse = loginRepository.getLocalLoginResponse().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        viewModelScope.launch {
            appPreference.readIsDarkMode().collect { mode ->
                mutableUiState.update { it.copy(isDarkMode = mode) }
            }
        }

        viewModelScope.launch {
            appPreference.readUserName().collect { userName ->
                mutableUiState.update { it.copy(userName = userName) }
            }
        }
        viewModelScope.launch {
            appPreference.readUserGmail().collect { userName ->
                mutableUiState.update { it.copy(gmail = userName) }
            }
        }

        /* viewModelScope.launch {
             loginResponse.collectLatest { response ->
                 if (response != null) {
                     mutableUiState.update { state -> state.copy(loginResponse = response) }
                 }
             }
         }*/
    }

    fun observeLoggedInStatus(isLoggedIn: (Boolean) -> Unit) {
        viewModelScope.launch {
            appPreference.readToken().collect { token ->
                Log.d(TAG, "observeLoggedInStatus: $token")
                mutableUiState.update { it.copy(token = token) }
                isLoggedIn(token.isNotEmpty())
            }
        }
    }

    fun updateUiState(uiState: MainUiState) {
        this.mutableUiState.update { uiState }
    }

    fun updateTitle(title: String) {
        this.mutableUiState.update { it.copy(title = title) }
    }

    fun updateIsDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            appPreference.isDarkMode(isDarkMode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            appPreference.saveToken("")
        }
    }

    fun updateApp(context: Context) {
        /*viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, isUpdating = true)
            if (uiState.token.isNullOrEmpty()) {
                uiState = uiState.copy(
                    isLoading = false,
                    message = Pair(false, "Token Expired Please Login Again")
                )
            } else {
                val response = appUpdateRepository.checkForUpdate(uiState.token ?: "")
                uiState = when (response) {
                    is Results.Error -> {
                        uiState.copy(
                            isLoading = false,
                            message = Pair(false, response.exception.message.toString()),
                            isUpdating = false
                        )
                    }

                    is Results.Success -> {
                        downloadApk(context, response.data.url)
                        Log.d(com.suffixit.bp.presentation.main.TAG, "updateApp: ${response.data.url} $context")
                        uiState.copy(update = response.data)
                    }
                }
            }
        }*/
    }

    private fun downloadApk(context: Context, url: String) {
        /* viewModelScope.launch {
             val apkFile = downloadApkToPrivateStorage(context, url)
             if (apkFile != null) {
                 Log.d(com.suffixit.bp.presentation.main.TAG, "APK downloaded to ${apkFile.absolutePath}")
                 // Optional: call install logic here
                 installApk(context, apkFile)
                 uiState = uiState.copy(isLoading = false, isUpdating = false)
             } else {
                 uiState = uiState.copy(
                     isLoading = false,
                     isUpdating = false,
                     message = Pair(false, "Download Failed")
                 )
                 Log.d(com.suffixit.bp.presentation.main.TAG, "Download failed, ")
             }
         }*/
    }

}

data class MainUiState(
    val isDarkMode: Boolean = false,
    val isLoading: Boolean = false,
    val token: String? = null,
    val message: String? = null,
    val userName: String? = null,
    // val loginResponse: LoginResponse? = null,
    val showLogoutDialog: Boolean = false,
    val title: String = "Home",
    val gmail: String = ""
)