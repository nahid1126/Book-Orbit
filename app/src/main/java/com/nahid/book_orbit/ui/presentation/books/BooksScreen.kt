package com.nahid.book_orbit.ui.presentation.books

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.ui.presentation.book_details.BookDetailsViewModel
import com.nahid.book_orbit.ui.presentation.component.CircularProgressDialog
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BooksScreen(
    sharedViewModel: MainViewModel,
    viewModel: BookDetailsViewModel = koinViewModel(),
    toPDFScreen: (String) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sharedViewModel.updateTitle("Books")
        viewModel.updateUiState(viewModel.uiState.copy(uId = sharedViewModel.uiState.value.userName))
        viewModel.getPurchaseBooks()
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
            if (viewModel.uiState.purchaseBooks.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No books available", color = Black)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.uiState.purchaseBooks!!.size) { book ->
                        BooksGridItem(book = viewModel.uiState.purchaseBooks!![book]) {
                            //toBookDetails(it)
                            toPDFScreen(it)
                        }
                    }
                }
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
                viewModel.updateUiState(viewModel.uiState.copy(message = null))
            }
        }
    }
}

@Composable
fun BooksGridItem(book: Book, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick(book.pdfUrl)
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
        Spacer(modifier = Modifier.height(8.dp))
    }
}