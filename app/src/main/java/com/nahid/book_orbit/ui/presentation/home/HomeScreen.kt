package com.nahid.book_orbit.ui.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.ui.presentation.component.CircularProgressDialog
import com.nahid.book_orbit.ui.presentation.component.ConfirmationDialog
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.White
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "HomeScreen"
@Composable
fun HomeScreen(
    sharedViewModel: MainViewModel,
    viewModel: HomeScreenViewModel = koinViewModel(),
    toBookDetails: (Book?) -> Unit,
    toExit: () -> Unit = {},
) {
    val context = LocalContext.current
    val sharedState by sharedViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.updateUiState(viewModel.uiState.copy(showExitDialog = true))
    }
    LaunchedEffect(Unit) {
        sharedViewModel.updateUiState(sharedState.copy(title = "Home"))
        viewModel.getAllBooks()
    }

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
                .background(White)
        ) {

            if (viewModel.uiState.books.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No books available")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items (viewModel.uiState.books) { book ->
                        BookGridItem(book = book){
                            toBookDetails(it)
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
        }

        if (viewModel.uiState.showExitDialog) {
            ConfirmationDialog(
                title = "Exit !",
                message = "Are You Sure Want to Exit ?",
                confirmText = "Exit",
                dismissText = "Cancel",
                onDismiss = {
                    viewModel.updateUiState(viewModel.uiState.copy(showExitDialog = false))
                },
                onConfirm = {
                    toExit()
                    viewModel.updateUiState(viewModel.uiState.copy(showExitDialog = false))
                }
            )
        }
    }
}

@Composable
fun BookGridItem(book: Book, onClick: (Book) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                Log.d(TAG, "BookGridItem: $book")
                onClick(book)
            }
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = book.coverImage,
            contentDescription = book.title,
            modifier = Modifier
                .aspectRatio(0.7f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))
        Card(
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(
                containerColor = if (book.isFree) Color(0xFFE0F2F1) else Color(0xFF4CAF50)
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = "Price",
                    modifier = Modifier.size(20.dp),
                    tint = White
                )

                Text(
                    text = if (book.isFree) "Free" else "${book.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}








