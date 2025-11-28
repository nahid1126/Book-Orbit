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
fun PrivacyAndPolicyScreen(sharedViewModel: MainViewModel) {
    val context = LocalContext.current
    sharedViewModel.updateTitle("Privacy Policy")
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
                "Privacy and Policy", style = Typography.titleLarge.copy(
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
                "Book Orbit (\"we,\" \"our,\" or \"us\") operates the mobile application (the \"App\"). This Privacy Policy explains how we collect, use, and protect your information.",
                style = Typography.bodyLarge.copy(
                    color = Black
                ),
                modifier = Modifier.padding(horizontal = AppConstants.APP_MARGIN.dp)
            )
            Text(
                "Information We Collect:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            BulletParagraph("Personal Information: Name, email address, mobile number, and shipping address (if applicable)")
            BulletParagraph("Usage Data: Log data, device type, operating system, and app usage statistics.")


            Text(
                "How We Use Your Information:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            BulletParagraph("To provide and maintain the app's services.")
            BulletParagraph("To communicate with you regarding updates, purchases, or customer support.")
            BulletParagraph("To analyze app usage and improve user experience.")

            Text(
                "Sharing Your Information:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            Text(
                "We do not sell your personal information. However, we may share your data:",
                style = Typography.bodyLarge.copy(
                    color = Black
                ),
                modifier = Modifier.padding(
                    start = (AppConstants.APP_MARGIN * 2).dp,
                    end = 4.dp,
                    bottom = 6.dp
                )
            )
            BulletParagraph("With service providers assisting in app operations (e.g., payment processors).")
            BulletParagraph("When required by law or to protect our legal rights.")

            Text(
                "Security:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            Text(
                "We employ industry-standard security measures to protect your data. However, no method of transmission over the internet is 100% secure.",
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
                "Your Rights:", style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
            )
            Text(
                "You may access, update, or delete your personal information by contacting us at support@bookorbit.online.",
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


