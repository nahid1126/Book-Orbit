package com.nahid.book_orbit.ui.presentation.gams

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.data.remote.dto.Gems
import com.nahid.book_orbit.ui.presentation.component.CircularProgressDialog
import com.nahid.book_orbit.ui.presentation.component.ConfirmationDialog
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import com.nahid.book_orbit.ui.theme.Olive
import com.nahid.book_orbit.ui.theme.White
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "GemsScreen"

@Composable
fun GemsScreen(sharedViewModel: MainViewModel, viewModel: GemsViewModel = koinViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sharedViewModel.updateTitle("Buy Gems")
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
            if (viewModel.uiState.gemsList.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No gems available", color = Black)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.uiState.gemsList!!) { book ->
                        GemsPurchaseItem(book){
                            viewModel.updateUiState(viewModel.uiState.copy(gemsId = it, showExitDialog = true))
                        }
                    }
                }
            }

            if (viewModel.uiState.isLoading) {
                CircularProgressDialog()
            }

            if (!viewModel.uiState.message.isNullOrEmpty()) {
                Toast.makeText(context, viewModel.uiState.message, Toast.LENGTH_SHORT).show()
                viewModel.updateUiState( viewModel.uiState.copy(message = null))
            }
            if (viewModel.uiState.showExitDialog){
                ConfirmationDialog(
                    title = "Warning !",
                    message = "Are You Sure Want to Buy The Gems ?",
                    confirmText = "Yes",
                    dismissText = "No",
                    onDismiss = {
                        viewModel.updateUiState(viewModel.uiState.copy(showExitDialog = false))
                    },
                    onConfirm = {
                        viewModel.buyGems()
                        viewModel.updateUiState(viewModel.uiState.copy(showExitDialog = false))
                    }
                )
            }
        }
    }
}

@Composable
fun GemsPurchaseItem(
    gems: Gems,
    onPurchaseClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Diamond,
            contentDescription = "Gems",
            modifier = Modifier.size(80.dp),
            tint = Color(0xFF_624_7AA)
        )

        Text(
            text = "${gems.gemsAmount}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(
            onClick = { onPurchaseClick(gems.id) },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            border = BorderStroke(2.dp, Black)
        ) {
            Text(
                text = "$${gems.usd}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

