package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.nahid.book_orbit.core.utils.AppConstants

@Composable
fun appTextStyle(
    color: Color = MaterialTheme.colorScheme.onPrimary,
    fontSize: Int = AppConstants.TEXT_SIZE_NORMAL
): TextStyle {
   return TextStyle(
        color = color,
        fontSize = fontSize.sp,
        fontFamily = FontFamily.Serif)
}