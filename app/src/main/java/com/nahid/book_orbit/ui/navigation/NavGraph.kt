package com.nahid.book_orbit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nahid.book_orbit.ui.presentation.books.BooksScreen
import com.nahid.book_orbit.ui.presentation.home.HomeScreen
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.presentation.profile.ProfileScreen

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
            HomeScreen(mainViewModel, toHome = {
                navController.navigate(Destinations.Home)
            }, toBooksItem = {
                navController.navigate(Destinations.Books)
            }, toProfile = {
                navController.navigate(Destinations.Profile)
            }, toExit = {
                onExit()
            })
        }

        composable<Destinations.Books> {
            onBottomNavigationChange(1)
            BooksScreen()
        }
        composable<Destinations.Profile> {
            onBottomNavigationChange(2)
            ProfileScreen()
        }

        /*composable<Destinations.CropInputItemCategory> {
            CropInputItemCategoryScreen(sharedViewModel = mainViewModel, toAreaWise = {
                navController.navigate(Destinations.AreaCropInput)
            }, toItemWise = {
                navController.navigate(Destinations.AreaCropInputSummary)
            })
        }

        composable<Destinations.AreaCropInputSummary> {
            CropInputSummaryScreen(mainViewModel, toArea = { id, name, uom ->
                navController.navigate(Destinations.AreaCropInputItem(id, name, uom))
            })
        }

        composable<Destinations.Area> {
            AreaScreen(mainViewModel, toZone = { id, name ->
                navController.navigate(Destinations.Zone(id, name))
            })
        }*/


    }

}