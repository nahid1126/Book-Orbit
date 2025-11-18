package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nahid.book_orbit.ui.theme.Red

@Composable
fun NoDataText(message: String = "No Data Found",color: Color = Red) {
    Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        Text(message, style = MaterialTheme.typography.titleMedium.copy(color = color))
    }
    
}