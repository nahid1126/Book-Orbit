package com.nahid.book_orbit.ui.presentation.welcome.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nahid.book_orbit.R
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.ui.presentation.component.CircularProgressDialog
import com.nahid.book_orbit.ui.theme.Black
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    toHome: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {

    val state = viewModel.uiState
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.googleLogin(idToken)
            }
        } catch (e: ApiException) {
            Log.d("LoginScreen", "Google sign in failed: ${e.statusCode}")
        }
    }

    LaunchedEffect(viewModel.uiState.isLoginSuccess) {
        if (viewModel.uiState.isLoginSuccess) {
            toHome()
        }
    }

    Scaffold { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.height(120.dp)
                    )
                    Spacer(modifier = Modifier.height(AppConstants.APP_MARGIN.dp))

                    if (!state.isLoading) {
                        OutlinedButton(
                            onClick = {
                                val gso =
                                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(AppConstants.WEB_CLIENT_ID)
                                        .requestEmail()
                                        .build()

                                val client = GoogleSignIn.getClient(context, gso)
                                client.signOut()
                                launcher.launch(client.signInIntent)
                            },
                            border = BorderStroke(1.dp, Black),
                            modifier = Modifier.fillMaxWidth(.80f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icons_google),
                                tint = Color.Unspecified,
                                contentDescription = null,
                                modifier = Modifier.padding(end = AppConstants.APP_MARGIN.dp)
                            )
                            Text("Continue With Google")
                        }

                    }


                }
            }

            if (state.message != null) {
                val message = state.message.second

                if (state.message.first) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
                viewModel.updateUiState(state.copy(message = null))
            }

            if (state.isLoading) {
                CircularProgressDialog()
            }
        }
    }
}

@Composable
fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.primary,
        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
        cursorColor = MaterialTheme.colorScheme.primary
    )
}
