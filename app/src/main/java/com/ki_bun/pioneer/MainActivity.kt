package com.ki_bun.pioneer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ki_bun.pioneer.data.AppDatabase
import com.ki_bun.pioneer.data.ItemDao
import com.ki_bun.pioneer.data.loadThemeMode
import com.ki_bun.pioneer.data.saveThemeMode
import com.ki_bun.pioneer.screens.HomeScreen
import com.ki_bun.pioneer.ui.theme.PioneerTheme
import com.ki_bun.pioneer.ui.theme.ThemeMode
import com.ki_bun.pioneer.viewmodel.ProgressViewModel
import com.ki_bun.pioneer.viewmodel.ProgressViewModelFactory
import kotlinx.coroutines.launch

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

            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    loadThemeMode(this@MainActivity).collect { savedTheme ->
                        themeMode = savedTheme
                        isThemeLoaded = true
                    }
                }
            }

                PioneerTheme(themeMode = themeMode) {
                   HomeScreen(
                       progressViewModel,
                       tags = progressList,
                       themeMode = themeMode,
                       onThemeModeChange = { selectedTheme ->
                        themeMode = selectedTheme
                        lifecycleScope.launch {
                            saveThemeMode(this@MainActivity, selectedTheme)
                        }
                    })
                }
        }
    }
}