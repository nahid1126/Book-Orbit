package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
fun PermissionDialog(message: String, onConfirm: () -> Unit) {

    AlertDialog(properties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
        onDismissRequest = {},
        title = {
            Text(
                text = "Permission Request !",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(message,style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Give Permission")
            }
        }
    )

}