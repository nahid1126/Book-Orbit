package com.nahid.book_orbit.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.ui.presentation.book_details.BookDetailsScreen
import com.nahid.book_orbit.ui.presentation.books.BooksScreen
import com.nahid.book_orbit.ui.presentation.books.PdfWebViewScreen
import com.nahid.book_orbit.ui.presentation.gams.GemsScreen
import com.nahid.book_orbit.ui.presentation.gams.HistoryScreen
import com.nahid.book_orbit.ui.presentation.home.HomeScreen
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.presentation.profile.ProfileScreen
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

private const val TAG = "NavGraph"

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestinations: Destinations = Destinations.Home,
    mainViewModel: MainViewModel,
    onBottomNavigationChange: (Int) -> Unit,
    onExit: () -> Unit,
) {

    NavHost(navController = navController, startDestination = startDestinations) {

        composable<Destinations.Home> {
            onBottomNavigationChange(0)
            HomeScreen(mainViewModel, toBookDetails = { book ->
                val finalBook = Json.encodeToJsonElement(book).toString()
                navController.navigate(Destinations.BookDetails(finalBook))
            }, toExit = {
                onExit()
            })
        }

        composable<Destinations.Books> {
            onBottomNavigationChange(1)
            BackHandler {
                navController.navigate(Destinations.Home) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            BooksScreen(sharedViewModel = mainViewModel, toPDFScreen = {
                val finalBook = Json.encodeToJsonElement(it).toString()
                navController.navigate(Destinations.PDFScreen(finalBook))
            })
        }
        composable<Destinations.PDFScreen> {
            val arguments = it.toRoute<Destinations.PDFScreen>()
            val finalBook = if (arguments.bookUrl == null) {
                null
            } else {
                Json.decodeFromString<String>(arguments.bookUrl)
            }
            PdfWebViewScreen(finalBook!!)
        }

        composable<Destinations.Profile> {
            onBottomNavigationChange(2)
            BackHandler {
                navController.navigate(Destinations.Home) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            ProfileScreen(sharedViewModel = mainViewModel)
        }

        composable<Destinations.BuyGems> {
            BackHandler {
                navController.navigate(Destinations.Home) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            GemsScreen(sharedViewModel = mainViewModel)
        }

        composable<Destinations.BookDetails> {
            val arguments = it.toRoute<Destinations.BookDetails>()
            val finalBook = if (arguments.book == null) {
                null
            } else {
                Json.decodeFromString<Book>(arguments.book)
            }
            BookDetailsScreen(finalBook, sharedViewModel = mainViewModel)
        }

        composable<Destinations.History> {
            BackHandler {
                navController.navigate(Destinations.Home) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            HistoryScreen(sharedViewModel = mainViewModel)
        }
    }

}