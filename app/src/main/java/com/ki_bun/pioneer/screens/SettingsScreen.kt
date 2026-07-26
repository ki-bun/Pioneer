package com.ki_bun.pioneer.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ki_bun.pioneer.R

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {
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
                Text(text = "  Settings", fontSize = 24.sp)
            }

            // Apperance and behavior
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier
                        .padding(5.dp)
                        .width(360.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    navController.navigate("preferencescreen")
                                }
                            )
                            .padding(vertical = 20.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.preference_24px),
                            contentDescription = "Preference icon",
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        ) {
                            Text(
                                text = "App preferences",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Modify the appearance and behavior",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Backup
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier
                        .padding(5.dp)
                        .width(360.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    navController.navigate("backupscreen")
                                }
                            )
                            .padding(vertical = 20.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.backup_24px),
                            contentDescription = "Backup icon",
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        ) {
                            Text(
                                text = "Backup",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Import and export your data",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // About
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier
                        .padding(5.dp)
                        .width(360.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    navController.navigate("aboutscreen")
                                }
                            )
                            .padding(vertical = 20.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.info_24px),
                            contentDescription = "Info icon",
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 20.dp)
                        ) {
                            Text(
                                text = "About",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Details about the app",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

            }
        }
    }
}