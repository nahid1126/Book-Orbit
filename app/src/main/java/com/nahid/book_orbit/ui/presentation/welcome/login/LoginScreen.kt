package com.nahid.book_orbit.ui.presentation.welcome.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.nahid.book_orbit.ui.theme.Olive
import com.nahid.book_orbit.ui.theme.White
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "LoginScreen"
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
            Log.d(TAG, "LoginScreen: $account")
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
        Image(
            painterResource(R.drawable.cover),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(.3f)
        )
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painterResource(R.drawable.cover),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.3f)
            )
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(AppConstants.APP_MARGIN.dp)
                            .height(200.dp)
                            .border(
                                width = 4.dp,
                                color = Black,
                                shape = RoundedCornerShape(50)
                            )
                            .clip(RoundedCornerShape(50))
                            .background(White)

                    )
                    if (!state.isLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 40.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
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
                                modifier = Modifier
                                    .fillMaxWidth(.80f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = White
                                ),
                                border = BorderStroke(2.dp, color = Black)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icons_google),
                                    tint = Color.Unspecified,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = AppConstants.APP_MARGIN.dp)
                                )
                                Text("Continue With Google", color = Black)
                            }
                        }

                    }


                }
            }

            if (!viewModel.uiState.message.isNullOrEmpty()) {
                Toast.makeText(context, viewModel.uiState.message, Toast.LENGTH_SHORT).show()
                viewModel.updateUiState( viewModel.uiState.copy(message = null))
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
