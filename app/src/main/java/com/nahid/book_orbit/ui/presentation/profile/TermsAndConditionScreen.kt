package com.nahid.book_orbit.ui.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.ui.presentation.component.BulletParagraph
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import com.nahid.book_orbit.ui.theme.Typography

@Composable
fun TermsAndConditionScreen(sharedViewModel: MainViewModel) {
    val context = LocalContext.current
    sharedViewModel.updateTitle("Terms & Conditions")
    val state = sharedViewModel.uiState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollState = rememberScrollState()

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = AppConstants.APP_MARGIN.dp,
                    vertical = (AppConstants.APP_MARGIN * 2).dp
                )
        ) {
            Text(
                "Terms & Conditions", style = Typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Black
                ), modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                "Effective Date: 11/12/2025", style = Typography.titleMedium.copy(
                    color = Black
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )

            Text(
                "By using the Book Orbit app, you agree to these Terms & Conditions. If you do not agree, please do not use the app.",
                style = Typography.bodyLarge.copy(
                    color = Black
                ),
                modifier = Modifier.padding(horizontal = AppConstants.APP_MARGIN.dp)
            )
            Text(
                "User Accounts:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            BulletParagraph("Users must register with a valid email address.")
            BulletParagraph("You are responsible for maintaining the confidentiality of your login credentials.")


            Text(
                "Service Usage:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            BulletParagraph("Users can purchase or read free ebooks via the app.")
            BulletParagraph("All content is provided “as is,” and we make no guarantees regarding its accuracy or availability.")

            Text(
                "Payments and Purchases:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            BulletParagraph("All purchases are final unless covered under our Refund Policy.")
            BulletParagraph("Prices for ebooks may change at any time without notice.")

            Text(
                "Intellectual Property:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            Text(
                "All app content, including ebooks, logos, and trademarks, are the property of Book Orbit or the respective copyright owners. You may not reproduce or distribute them without permission.",
                style = Typography.bodyLarge.copy(
                    color = Black
                ),
                modifier = Modifier.padding(
                    start = (AppConstants.APP_MARGIN * 2).dp,
                    end = 4.dp,
                    bottom = 6.dp
                )
            )

            Text(
                "Termination:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            Text(
                "We may suspend or terminate your access to the app if you violate these terms.",
                style = Typography.bodyLarge.copy(
                    color = Black
                ),
                modifier = Modifier.padding(
                    start = (AppConstants.APP_MARGIN * 2).dp,
                    end = 4.dp,
                    bottom = 6.dp
                )
            )
        }
    }
}