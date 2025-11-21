package com.nahid.book_orbit.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nahid.book_orbit.ui.presentation.welcome.login.LoginScreen
import com.nahid.book_orbit.ui.presentation.welcome.splash.SplashScreen

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    startDestinations: Destinations = Destinations.SplashScreen, onFinish: () -> Unit = {},
) {

    NavHost(navController = navController, startDestination = startDestinations) {
        composable<Destinations.SplashScreen> {
            SplashScreen(toLogin = {
                navController.navigate(Destinations.LoginScreen) {
                    popUpTo(Destinations.SplashScreen) {
                        inclusive = true
                    }
                }

            }, toHome = {
                onFinish()
            })
        }

        composable<Destinations.LoginScreen> {
            LoginScreen(toHome = {
                onFinish()
            }
            )
        }
    }

}