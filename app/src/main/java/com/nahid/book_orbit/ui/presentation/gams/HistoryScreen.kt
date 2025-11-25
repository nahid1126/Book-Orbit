package com.nahid.book_orbit.ui.presentation.gams

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.data.remote.dto.GemsTransaction
import com.nahid.book_orbit.ui.presentation.component.AnimatedProgressDialog
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryScreen(sharedViewModel: MainViewModel, viewModel: GemsViewModel = koinViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sharedViewModel.updateTitle("History")
        viewModel.updateUiState(viewModel.uiState.copy(uId = sharedViewModel.uiState.value.userName))
        viewModel.getHistory()
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
            if (viewModel.uiState.transactionHistory.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No History Found", color = Black)
                }
            } else {
                LazyColumn {
                    items(viewModel.uiState.transactionHistory!!) {
                        TransactionHistoryRow(it)
                    }
                }
            }

            if (viewModel.uiState.isLoading) {
                AnimatedProgressDialog()
            }
            if (!viewModel.uiState.message.isNullOrEmpty()) {
                Toast.makeText(context, viewModel.uiState.message, Toast.LENGTH_SHORT).show()
                viewModel.updateUiState( viewModel.uiState.copy(message = null))
            }
        }
    }
}

@Composable
fun TransactionHistoryRow(
    transaction: GemsTransaction,
    modifier: Modifier = Modifier
) {
    val formattedDate = transaction.timestamp?.toDate()?.let {
        SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a", Locale.getDefault()).format(it)
    } ?: "Date not available"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Gems Purchased",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "+${transaction.gemsAmount}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

