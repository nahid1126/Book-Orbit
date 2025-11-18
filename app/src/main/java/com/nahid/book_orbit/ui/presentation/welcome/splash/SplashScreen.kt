package com.nahid.book_orbit.ui.presentation.welcome.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nahid.book_orbit.R

import com.nahid.book_orbit.data.local.AppPreference
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Preview
@Composable
fun SplashScreen(toLogin: () -> Unit = {}, toHome: () -> Unit = {}) {
    val dataStore = koinInject<AppPreference>()
    LaunchedEffect(Unit) {
        delay(1000)
        dataStore.readToken().collect {
            if (it.isEmpty()) {
                toLogin()
            } else {
                toHome()
            }
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }

}