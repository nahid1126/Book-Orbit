package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.ui.theme.Black

@Composable
fun BulletParagraph(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "\u2022",
            fontSize = 20.sp,
            color = Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                end = 8.dp,
                start = (AppConstants.APP_MARGIN * 4).dp
            )
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp)
        )
    }
}