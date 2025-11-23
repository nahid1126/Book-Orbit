package com.nahid.book_orbit.ui.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.core.utils.Logger
import com.nahid.book_orbit.core.utils.NavigationDestinations
import com.nahid.book_orbit.ui.navigation.Destinations
import com.nahid.book_orbit.ui.navigation.NavGraph
import com.nahid.book_orbit.ui.presentation.component.AnimatedProgressDialog
import com.nahid.book_orbit.ui.presentation.component.ConfirmationDialog
import com.nahid.book_orbit.ui.presentation.welcome.WelcomeActivity
import com.nahid.book_orbit.ui.theme.SuffixSurveyTheme
import com.nahid.book_orbit.ui.theme.ToolbarTextColor
import com.nahid.book_orbit.ui.theme.Typography
import com.nahid.book_orbit.ui.theme.White
import org.koin.compose.viewmodel.koinViewModel


private const val TAG = "SurveyMainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            SuffixSurveyTheme(darkTheme = false, dynamicColor = false) {

                viewModel.observeLoggedInStatus {
                    if (!it) {
                        val intent = Intent(this, WelcomeActivity::class.java)
                        intent.putExtra(AppConstants.INITIAL_START, false)
                        startActivity(intent)
                        finish()
                    }
                }
                if (state != null) {
                    App(viewModel, state!!)
                }
            }
        }
    }


    @SuppressLint("IntentReset")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun App(viewModel: MainViewModel, uiState: MainUiState) {

        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val context = this
        val currentRoute = currentBackStack?.destination?.route
        Logger.log(TAG, "currentRoute $currentRoute ${Destinations.Search}")

        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableIntStateOf(0) }

        if (!uiState.message.isNullOrEmpty()) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            viewModel.updateUiState(uiState.copy(message = null))
        }
        if (uiState.isLoading) {
            AnimatedProgressDialog()
        }
        var selectedNavigationDestination by remember { mutableIntStateOf(0) }
        LaunchedEffect(Unit) {
            viewModel.readGems()
        }
        if (uiState.showLogoutDialog) {
            ConfirmationDialog(
                title = "Logout",
                message = "Are You Sure Want to Logout ?",
                onDismiss = {
                    viewModel.updateUiState(uiState.copy(showLogoutDialog = false))
                },
                onConfirm = {
                    viewModel.logout()
                    viewModel.updateUiState(uiState.copy(showLogoutDialog = false))
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        val title = uiState.title

                        Column {
                            Text(
                                title,
                                style = if (title.contains("Dashboard")) Typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ) else Typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            )

                        }


                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = ToolbarTextColor
                    ),
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = (AppConstants.APP_MARGIN * 2).dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate(Destinations.BuyGems)
                                    }
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .background(
                                        color = White,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .clip(RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Diamond,
                                    contentDescription = "Diamonds",
                                    tint = Color.Red,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Log.d(TAG, "App: ${uiState.gems}")
                                Text(
                                    text = uiState.gems.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                             if ((currentRoute ?: "").contains(
                                     Destinations.BuyGems.toString(),
                                     ignoreCase = true
                                 )
                             ) {
                                 Box {
                                     IconButton(onClick = { navController.navigate(Destinations.History) }) {
                                         Icon(
                                             Icons.Default.FileCopy,
                                             contentDescription = null,
                                             tint = White
                                         )
                                     }
                                     }
                                 }
                        }
                    }, navigationIcon = {
                        if (!(currentRoute ?: "").contains(
                                Destinations.Home.toString(),
                                ignoreCase = true
                            )
                        ) {

                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(
                                        end = (AppConstants.APP_MARGIN / 2).dp
                                    )
                                    .clickable {
                                        val entry = navController.previousBackStackEntry
                                        if (entry != null) {
                                            navController.popBackStack()
                                        }
                                    }
                            )
                        }
                    }, modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                )
                //
            },
            bottomBar = {
                if (uiState.title.equals(
                        NavigationDestinations.HOME.value,
                        true
                    ) || uiState.title.equals(
                        NavigationDestinations.Books.value,
                        true
                    ) || uiState.title.equals(NavigationDestinations.PROFILE.value, true)
                ) {
                    NavigationBar(
                        windowInsets = NavigationBarDefaults.windowInsets,
                    ) {

                        NavigationDestinations.entries.forEachIndexed { index, destination ->
                            val selected = index == selectedNavigationDestination

                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    selectedNavigationDestination = index
                                    when (selectedNavigationDestination) {
                                        0 -> {
                                            navController.navigate(Destinations.Home)
                                        }

                                        1 -> {
                                            navController.navigate(Destinations.Books)
                                        }

                                        2 -> {
                                            navController.navigate(Destinations.Profile)
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        if (selected) {
                                            destination.selectedIcon
                                        } else {
                                            destination.icon
                                        }, contentDescription = destination.value
                                    )

                                }, label = {
                                    Text(text = destination.value)
                                }
                            )

                        }

                    }
                }
            },
        ) { innerPadding ->

            Box(Modifier.padding(innerPadding)) {
                NavGraph(
                    navController = navController,
                    mainViewModel = viewModel,
                    onBottomNavigationChange = {
                        selectedItem = it
                    }, onExit = {
                        finish()
                    })
            }

        }

    }
}
