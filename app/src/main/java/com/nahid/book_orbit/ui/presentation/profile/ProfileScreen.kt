package com.nahid.book_orbit.ui.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment

@Composable
fun ProfileScreen(sharedViewModel: MainViewModel) {
    sharedViewModel.updateTitle("Profile")
    val state = sharedViewModel.uiState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = (AppConstants.APP_MARGIN).dp)
        ) {
            if (state.value.gmail.isNotEmpty()) {
                Text(text = state.value.gmail, color = Black, modifier = Modifier.padding(AppConstants.APP_MARGIN.dp))
            }
            Button(
                onClick = {
                    sharedViewModel.updateUiState(
                        sharedViewModel.uiState.value.copy(
                            showLogoutDialog = true
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)
            ) {
                Text("logout")
            }
        }
    }
}
