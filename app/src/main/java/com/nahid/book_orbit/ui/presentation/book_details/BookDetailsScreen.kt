package com.nahid.book_orbit.ui.presentation.book_details


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.ui.presentation.component.CircularProgressDialog
import com.nahid.book_orbit.ui.presentation.component.ConfirmationDialog
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import com.nahid.book_orbit.ui.theme.White
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "BookDetailsScreen"
@Composable
fun BookDetailsScreen(
    finalBook: Book?,
    sharedViewModel: MainViewModel,
    viewModel: BookDetailsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sharedViewModel.updateTitle("Book Details")
        viewModel.updateUiState(
            viewModel.uiState.copy(
                uId = sharedViewModel.uiState.value.userName,
                currentGems = sharedViewModel.uiState.value.gems ?: 0
            )
        )
        Log.d(TAG, "BookDetailsScreen: ${sharedViewModel.uiState.value.userName}")
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = (AppConstants.APP_MARGIN).dp).verticalScroll(rememberScrollState())
        ) {
            finalBook?.let {
                viewModel.updateUiState(viewModel.uiState.copy(book = it))
                AsyncImage(
                    model = it.coverImage,
                    contentDescription = it.title,
                    modifier = Modifier
                        .padding(
                            horizontal = (AppConstants.APP_MARGIN * 8).dp,
                            vertical = (AppConstants.APP_MARGIN * 4).dp
                        )
                        .aspectRatio(0.7f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it.shortDesc,
                    fontSize = 14.sp,
                    color = Black,
                    modifier = Modifier.padding(horizontal = (AppConstants.APP_MARGIN * 4).dp)
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = (AppConstants.APP_MARGIN * 4).dp,
                            vertical = AppConstants.APP_MARGIN.dp
                        ),
                    onClick = {
                        viewModel.updateUiState(viewModel.uiState.copy(isBuyClicked = true))
                    }) {
                    Text("Buy Now", color = White)
                }
            }

            if (viewModel.uiState.isBuyClicked) {
                ConfirmationDialog(
                    title = "Warning !",
                    message = "Are You Sure Want to Buy this book ?",
                    confirmText = "Yes",
                    dismissText = "No",
                    onDismiss = {
                        viewModel.updateUiState(viewModel.uiState.copy(isBuyClicked = false))
                    },
                    onConfirm = {
                        viewModel.purchasesBook()
                        viewModel.updateUiState(viewModel.uiState.copy(isBuyClicked = false))
                    }
                )
            }
            if (viewModel.uiState.isLoading) {
                CircularProgressDialog()
            }


            if (viewModel.uiState.message != null) {
                Toast.makeText(
                    context,
                    "${viewModel.uiState.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "BookDetailsScreen: ${viewModel.uiState.message}")
                viewModel.updateUiState(viewModel.uiState.copy(message = null))
            }
        }
    }
}