package com.nahid.book_orbit.ui.presentation.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.ui.presentation.component.CircularProgressDialog
import com.nahid.book_orbit.ui.presentation.component.ConfirmationDialog
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
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
    val sharedState by sharedViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.updateUiState(viewModel.uiState.copy(showExitDialog = true))
    }
    LaunchedEffect(Unit) {
        sharedViewModel.updateTitle("Home")
        viewModel.getAllBooks()
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
                .background(White)
        ) {

            /*Column(
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



                }
            }*/
            if (viewModel.uiState.books.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No books available")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.uiState.books.size) { book ->
                        BookGridItem(book = viewModel.uiState.books[book]){
                            // toBooksItem()
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            if (viewModel.uiState.isLoading) {
                CircularProgressDialog()
            }

            if (viewModel.uiState.exception != null) {
                Toast.makeText(
                    context,
                    "${viewModel.uiState.exception!!.message}",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.updateUiState(viewModel.uiState.copy(exception = null))
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
fun BookGridItem(book: Book, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(book.id) }
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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = book.title,
            fontSize = 14.sp,
            maxLines = 1,
            color = Black
        )

        if (!book.isFree) {
            Text(
                text = "${book.price}৳",
                fontSize = 12.sp,
                color = Color.Red
            )
        } else {
            Text(
                text = "Free",
                fontSize = 12.sp,
                color = Color.Green
            )
        }
    }
}







