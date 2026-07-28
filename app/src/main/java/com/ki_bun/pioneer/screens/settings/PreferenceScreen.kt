package com.ki_bun.pioneer.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.ui.theme.ThemeMode

@Composable
fun PreferenceScreen(
    navController: NavController,
    onThemeModeChange: (ThemeMode) -> Unit,
    themeMode: ThemeMode
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(painterResource(
                        id = R.drawable.arrow_back_24px),
                        contentDescription = "Back button"
                    )
                }
                Text(text = "Preferences", fontSize = 24.sp, modifier = Modifier.padding(start = 5.dp))
            }

            Text(
                text = "Theme",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(x = 30.dp)
            )
            Surface(modifier = Modifier.clickable {
                expanded = !expanded
            }) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(x = (-30).dp, y = 0.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Auto") },
                        onClick = {
                            onThemeModeChange(ThemeMode.AUTO)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Light") },
                        onClick = {
                            onThemeModeChange(ThemeMode.LIGHT)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Dark") },
                        onClick = {
                            onThemeModeChange(ThemeMode.DARK)
                            expanded = false
                        }
                    )
                }
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                    Text(
                        text = "App theme",
                        fontSize = 18.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                    Text(
                        text = when (themeMode) {
                            ThemeMode.AUTO -> {
                                "Auto"
                            }
                            ThemeMode.DARK -> {
                                "Dark"
                            }
                            else -> {
                                "Light"
                            }
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                }
            }
        }
    }
}