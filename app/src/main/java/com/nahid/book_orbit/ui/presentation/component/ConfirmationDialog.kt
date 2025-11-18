package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.ui.theme.Primary
import com.nahid.book_orbit.ui.theme.White
import org.koin.dsl.module

@Preview
@Composable
fun ConfirmationDialog(
    isDarkMode: Boolean = false,
    icon: ImageVector? = null,
    title: String = "",
    message: String = "",
    confirmText: String = "Confirm",
    dismissText: String = "Dismiss",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {

    AlertDialog(containerColor = White,
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
        },
        text = {
            Text(text = message,style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(confirmText, color = Primary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(dismissText, color = Primary)
            }
        },
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false),
    )

}