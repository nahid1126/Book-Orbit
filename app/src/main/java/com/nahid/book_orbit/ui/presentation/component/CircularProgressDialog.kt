package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nahid.book_orbit.ui.theme.LightGrey

@Composable
fun CircularProgressDialog() {
    Dialog(onDismissRequest = {

    }, properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
                trackColor = LightGrey,
                strokeCap = StrokeCap.Round
            )
        }
    }
}