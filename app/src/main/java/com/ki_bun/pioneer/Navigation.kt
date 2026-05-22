package com.ki_bun.pioneer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ki_bun.pioneer.screens.HomeScreen
import com.ki_bun.pioneer.screens.SettingsScreen
import com.ki_bun.pioneer.screens.TagsScreen
import com.ki_bun.pioneer.ui.theme.ThemeMode
import com.ki_bun.pioneer.viewmodel.ProgressViewModel

@Composable
fun MyAppNavHost(
    progressViewModel: ProgressViewModel,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
    ) {

    val progressList by progressViewModel.progressList.collectAsState()
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        // Home button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (selectedIndex == 0) {
                                            MaterialTheme.colorScheme.secondaryContainer
                                        } else Color.Transparent, shape = CircleShape
                                    )
                                    .width(60.dp)
                                    .height(35.dp)
                            ) {
                                IconButton(
                                    modifier = Modifier.width(60.dp),
                                    onClick = {
                                        selectedIndex = 0
                                        navController.navigate("homescreen")
                                    }
                                ) {
                                    Icon(
                                        painter = if (selectedIndex == 0) {
                                            painterResource(id = R.drawable.filled_home_24px)
                                        } else {
                                            painterResource(id = R.drawable.home_24px)
                                        },
                                        contentDescription = "Home",
                                        tint = if (selectedIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Text(
                                text = "Home",
                                fontSize = 12.sp,
                                color = if (selectedIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Tags button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (selectedIndex == 1) {
                                            MaterialTheme.colorScheme.secondaryContainer
                                        } else Color.Transparent, shape = CircleShape
                                    )
                                    .width(60.dp)
                                    .height(35.dp)
                            ) {
                                IconButton(
                                    modifier = Modifier.width(60.dp),
                                    onClick = {
                                        selectedIndex = 1
                                        navController.navigate("tagsscreen")
                                    }
                                ) {
                                    Icon(
                                        painter = if (selectedIndex == 1) {
                                            painterResource(id = R.drawable.tag_filled_24px)
                                        } else {
                                            painterResource(id = R.drawable.tag_24px)
                                        },
                                        contentDescription = "Tags",
                                        tint = if (selectedIndex == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Text(
                                text = "Tags",
                                fontSize = 12.sp,
                                color = if (selectedIndex == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Settings button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (selectedIndex == 2) {
                                            MaterialTheme.colorScheme.secondaryContainer
                                        } else Color.Transparent, shape = CircleShape
                                    )
                                    .width(60.dp)
                                    .height(35.dp)
                            ) {
                                IconButton(
                                    modifier = Modifier.width(60.dp),
                                    onClick = {
                                        selectedIndex = 2
                                        navController.navigate("settings")
                                    }
                                ) {
                                    Icon(
                                        painter = if (selectedIndex == 2) {
                                            painterResource(id = R.drawable.filled_settings_24px)
                                        } else {
                                            painterResource(id = R.drawable.settings_24px)
                                        },
                                        contentDescription = "Settings",
                                        tint = if (selectedIndex == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Text(
                                text = "Settings",
                                fontSize = 12.sp,
                                color = if (selectedIndex == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "homescreen",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("homescreen") {
                HomeScreen(progressViewModel)
            }
            composable("tagsscreen") {
                    TagsScreen(tags = progressList)
            }
            composable("settings") {
                SettingsScreen(
                    onThemeModeChange = onThemeModeChange,
                    themeMode = themeMode,
                    progressViewModel = progressViewModel
                )
            }
        }
    }
}