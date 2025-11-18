package com.nahid.book_orbit.ui.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nahid.book_orbit.R
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.core.utils.Logger
import com.nahid.book_orbit.ui.presentation.component.ConfirmationDialog
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import com.nahid.book_orbit.ui.theme.Typography
import com.nahid.book_orbit.ui.theme.White
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    sharedViewModel: MainViewModel,
    viewModel: HomeScreenViewModel = koinViewModel(),
    toHome: () -> Unit = {},
    toBooksItem: () -> Unit = {},
    toProfile: () -> Unit = {},
    toExit: () -> Unit = {},
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedState by sharedViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.updateUiState(state.copy(showExitDialog = true))
    }

    /*LaunchedEffect(Unit) {
        sharedViewModel.updateUiState(sharedViewModel.uiState.value.copy(title = ""))
    }*/
    /*LaunchedEffect(sharedState.loginResponse != null) {
        Logger.log("HomeScreen", "HomeScreen: Launch ${sharedViewModel.loginResponse.value}")
        viewModel.updateUiState(uiState = viewModel.uiState.value.copy(loginResponse = sharedViewModel.loginResponse.value))
    }*/



    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(
                        shape = RoundedCornerShape(
                            topEnd = (AppConstants.APP_MARGIN * 4).dp,
                            topStart = (AppConstants.APP_MARGIN * 4).dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Bottom
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    /*DashboardButton(
                        title = "Farmer",
                        painter = painterResource(R.drawable.ic_farmer),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = (AppConstants.APP_MARGIN).dp)
                            .clip(shape = RoundedCornerShape((AppConstants.APP_MARGIN * 2).dp))
                    ) {
                        Toast.makeText(context, "Work in Progress", Toast.LENGTH_SHORT).show()
                    }*/


                }
            }
        }

        if (state.showExitDialog) {
            ConfirmationDialog(
                title = "Exit !",
                message = "Are You Sure Want to Exit ?",
                confirmText = "Exit",
                dismissText = "Cancel",
                onDismiss = {
                    viewModel.updateUiState(state.copy(showExitDialog = false))
                },
                onConfirm = {
                    toExit()
                    viewModel.updateUiState(state.copy(showExitDialog = false))
                }
            )
        }
    }
}





