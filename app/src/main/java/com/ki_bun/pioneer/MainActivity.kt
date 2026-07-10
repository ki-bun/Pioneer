package com.ki_bun.pioneer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ki_bun.pioneer.data.AppDatabase
import com.ki_bun.pioneer.data.ItemDao
import com.ki_bun.pioneer.data.loadThemeMode
import com.ki_bun.pioneer.data.saveThemeMode
import com.ki_bun.pioneer.screens.HomeScreen
import com.ki_bun.pioneer.screens.SettingsScreen
import com.ki_bun.pioneer.screens.settings.BackupScreen
import com.ki_bun.pioneer.screens.settings.PreferenceScreen
import com.ki_bun.pioneer.ui.theme.PioneerTheme
import com.ki_bun.pioneer.ui.theme.ThemeMode
import com.ki_bun.pioneer.viewmodel.ProgressViewModel
import com.ki_bun.pioneer.viewmodel.ProgressViewModelFactory
import kotlinx.coroutines.launch

enum class Status {
    IN_PROGRESS,
    COMPLETED
}

class MainActivity : ComponentActivity() {

    private val itemDao: ItemDao by lazy {
        AppDatabase.getDatabase(applicationContext).itemDao()
    }
    private val progressViewModel: ProgressViewModel by viewModels {
        ProgressViewModelFactory(itemDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var isThemeLoaded = false
        splashScreen.setKeepOnScreenCondition {
            !isThemeLoaded
        }
        enableEdgeToEdge()
        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.AUTO) }
            val progressList by progressViewModel.progressList.collectAsState()
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    loadThemeMode(this@MainActivity).collect { savedTheme ->
                        themeMode = savedTheme
                        isThemeLoaded = true
                    }
                }
            }

                PioneerTheme(themeMode = themeMode) {
                    Surface {
                        NavHost(
                            navController = navController,
                            startDestination = "homescreen",
                        ) {
                            composable("homescreen") {
                                HomeScreen(
                                    progressViewModel,
                                    progress = progressList,
                                    navController
                                )
                            }
                            composable("settings") {
                                SettingsScreen(
                                    navController
                                )
                            }
                            composable("backupscreen") {
                                BackupScreen(progressViewModel)
                            }
                            composable("preferencescreen") {
                                PreferenceScreen(onThemeModeChange = { selectedTheme ->
                                    themeMode = selectedTheme
                                    lifecycleScope.launch {
                                        saveThemeMode(this@MainActivity, selectedTheme)
                                    }
                                }, themeMode)
                            }
                        }
                    }
                }
        }
    }
}