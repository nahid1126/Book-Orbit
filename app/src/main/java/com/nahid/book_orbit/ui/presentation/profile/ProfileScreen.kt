package com.nahid.book_orbit.ui.presentation.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.core.net.toUri
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.theme.Black
import com.nahid.book_orbit.ui.theme.Typography
import com.nahid.book_orbit.ui.theme.White

private const val TAG = "ProfileScreen"
@Composable
fun ProfileScreen(
    sharedViewModel: MainViewModel,
    toPrivacyAndPolicy: () -> Unit,
    toTermsAndCondition: () -> Unit
) {
    val context = LocalContext.current
    sharedViewModel.updateTitle("Profile")
    val state = sharedViewModel.uiState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = (AppConstants.APP_MARGIN).dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height((AppConstants.APP_MARGIN * 4).dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(90.dp)
            )
            if (state.value.gmail.isNotEmpty()) {
                Text(
                    text = state.value.gmail,
                    color = Black,
                    modifier = Modifier.padding(AppConstants.APP_MARGIN.dp)
                )
            }
            Button(
                onClick = {
                    sharedViewModel.updateUiState(
                        sharedViewModel.uiState.value.copy(
                            showLogoutDialog = true
                        )
                    )
                },
                border = BorderStroke(2.dp, Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 80.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = White,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text("Logout", color = White)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(
                        vertical = (AppConstants.APP_MARGIN * 4).dp,
                        horizontal = 20.dp
                    )
                )
                Text(
                    "Terms & Policies",
                    style = Typography.titleMedium.copy(
                        color = Black,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(
                        start = 26.dp,
                        bottom = (AppConstants.APP_MARGIN * 2).dp
                    )
                )
                Text(
                    "Terms & Conditions",
                    style = Typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(
                            start = 34.dp,
                            bottom = (AppConstants.APP_MARGIN).dp
                        )
                        .clickable {
                            toTermsAndCondition()
                        }
                )
                Text(
                    "Privacy Policy",
                    style = Typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(
                            start = 34.dp,
                            top = 4.dp,
                            bottom = (AppConstants.APP_MARGIN).dp
                        )
                        .clickable {
                            toPrivacyAndPolicy()
                        }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(
                        vertical = (AppConstants.APP_MARGIN * 4).dp,
                        horizontal = 20.dp
                    )
                )
                Text(
                    "Help Us Grow",
                    style = Typography.titleMedium.copy(
                        color = Black,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(
                        start = 26.dp,
                        bottom = (AppConstants.APP_MARGIN * 2).dp
                    )
                )
                Text(
                    "Share App",
                    style = Typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "https://play.google.com/store/apps/details?id=${context.packageName}"
                                )
                                type = "text/plain"
                            }
                            val shareIntent =
                                Intent.createChooser(sendIntent, "Share Book Orbit App")
                            context.startActivity(shareIntent)
                        }
                        .padding(
                            start = 34.dp,
                            bottom = (AppConstants.APP_MARGIN).dp
                        )
                )
                Text(
                    "Rate Us on Google Play",
                    style = Typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(
                            start = 34.dp,
                            top = 4.dp,
                            bottom = (AppConstants.APP_MARGIN).dp
                        )
                        .clickable {
                            val appPackageName = context.packageName
                            try {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        "market://details?id=$appPackageName".toUri()
                                    )
                                )
                            } catch (e: ActivityNotFoundException) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        "https://play.google.com/store/apps/details?id=$appPackageName".toUri()
                                    )
                                )
                            }

                        }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(
                        vertical = (AppConstants.APP_MARGIN * 4).dp,
                        horizontal = 20.dp
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "\u00A9 Book Orbit",
                style = Typography.bodyLarge.copy(
                    color = Black
                ),
                modifier = Modifier.padding(
                    top = 4.dp
                )
            )
        }
    }
}
