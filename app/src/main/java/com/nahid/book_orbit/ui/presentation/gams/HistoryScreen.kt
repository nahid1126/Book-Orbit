package com.nahid.book_orbit.ui.presentation.gams

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryScreen(sharedViewModel: MainViewModel, viewModel: GemsViewModel = koinViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sharedViewModel.updateTitle("History")
        viewModel.updateUiState(viewModel.uiState.copy(uId = sharedViewModel.uiState.value.userName))
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = (AppConstants.APP_MARGIN).dp)
        ) {
            
        }
    }
}