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
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nahid.book_orbit.core.utils.AppConstants
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
                .padding(horizontal = AppConstants.APP_MARGIN.dp, vertical = (AppConstants.APP_MARGIN * 2).dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    // --- 1. Define reusable styles ---
                    val headingStyle = Typography.titleMedium.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                    val bodyStyle = Typography.bodyLarge.copy(lineHeight = 24.sp).toSpanStyle()
                    // Style for indented bullet points
                    val bulletPointStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))

                    withStyle(style = Typography.titleLarge.toSpanStyle().copy(fontWeight = FontWeight.Bold)) {
                        append("Privacy and Policy\n")
                    }
                    withStyle(style = headingStyle) {
                        append("Effective Date: 11/12/2025\n\n")
                    }

                    // Introduction
                    withStyle(style = bodyStyle) {
                        append("Book Orbit (\"we,\" \"our,\" or \"us\") operates the mobile application (the \"App\"). This Privacy Policy explains how we collect, use, and protect your information.\n\n")
                    }

                    // --- 2. Apply styles correctly ---

                    // Information We Collect
                    withStyle(style = headingStyle) {
                        append("Information We Collect:\n")
                    }
                    withStyle(style = bulletPointStyle) { // Apply bullet style here
                        withStyle(style = bodyStyle) {
                            append("• Personal Information: Name, email address, mobile number, and shipping address (if applicable).\n")
                            append("• Usage Data: Log data, device type, operating system, and app usage statistics.\n\n")
                        }
                    }

                    // How We Use Your Information
                    withStyle(style = headingStyle) {
                        append("How We Use Your Information:\n")
                    }
                    withStyle(style = bulletPointStyle) {
                        withStyle(style = bodyStyle) {
                            append("• To provide and maintain the app's services.\n")
                            append("• To communicate with you regarding updates, purchases, or customer support.\n")
                            append("• To analyze app usage and improve user experience.\n\n")
                        }
                    }

                    // Sharing Your Information
                    withStyle(style = headingStyle) {
                        append("Sharing Your Information:\n")
                    }
                    withStyle(style = bodyStyle) {
                        append("We do not sell your personal information. However, we may share your data:\n")
                    }
                    withStyle(style = bulletPointStyle) {
                        withStyle(style = bodyStyle) {
                            append("• With service providers assisting in app operations (e.g., payment processors).\n")
                            append("• When required by law or to protect our legal rights.\n\n")
                        }
                    }

                    // Security
                    withStyle(style = headingStyle) {
                        append("Security:\n")
                    }
                    withStyle(style = bodyStyle) {
                        append("We employ industry-standard security measures to protect your data. However, no method of transmission over the internet is 100% secure.\n\n")
                    }

                    // Your Rights
                    withStyle(style = headingStyle) {
                        append("Your Rights:\n")
                    }
                    withStyle(style = bodyStyle) {
                        append("You may access, update, or delete your personal information by contacting us at support@eboi.online.")
                    }
                },
                textAlign = TextAlign.Justify,
                color = Black,
                modifier = Modifier.padding(bottom = (AppConstants.APP_MARGIN).dp)
            )
        }
    }
}