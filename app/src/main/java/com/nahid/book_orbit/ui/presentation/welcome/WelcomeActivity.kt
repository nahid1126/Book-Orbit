package com.nahid.book_orbit.ui.presentation.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.nahid.book_orbit.ui.navigation.Destinations
import com.nahid.book_orbit.ui.navigation.SetUpNavGraph
import com.nahid.book_orbit.ui.presentation.main.MainActivity
import com.nahid.book_orbit.ui.theme.SuffixSurveyTheme
import com.nahid.book_orbit.core.utils.AppConstants

private const val TAG = "WelcomeActivity"

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuffixSurveyTheme(dynamicColor = false) {
                val initialStart = intent.getBooleanExtra(AppConstants.INITIAL_START, true)
                App(initialStart)
            }
        }
    }

    @Composable
    fun App(initialStart: Boolean) {
        val navController = rememberNavController()
        val context = LocalContext.current
        SetUpNavGraph(navController, startDestinations = if (initialStart) Destinations.SplashScreen else Destinations.LoginScreen){
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }
    }
}
