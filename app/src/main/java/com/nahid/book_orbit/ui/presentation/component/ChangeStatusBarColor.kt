package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ChangeStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = color)
    //systemUiController.isStatusBarVisible = false //Hide StatusBar
}