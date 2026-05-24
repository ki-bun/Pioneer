package com.ki_bun.pioneer.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.viewmodel.ProgressViewModel
import com.ki_bun.pioneer.ui.theme.ThemeMode

@Composable
fun SettingsScreen(
    onThemeModeChange: (ThemeMode) -> Unit,
    themeMode: ThemeMode,
    progressViewModel: ProgressViewModel,
    navController: NavController
) {

    val uriHandler = LocalUriHandler.current
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) {
            progressViewModel.exportToUri(context, uri)
        }
    }

    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { progressViewModel.importFromCSV(context, it) }
        }
    )

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
            Text(
                text = "Theme",
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
            Text(
                text = "Backup",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(x = 30.dp)
            )
            Surface(
                modifier = Modifier.clickable {
                    createFileLauncher.launch("pioneer_export.csv")
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                    Text(
                        text = "Export to CSV",
                        fontSize = 18.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                    Text(
                        text = "Backup your data",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                }
            }
            Surface(
                modifier = Modifier.clickable {
                    openFileLauncher.launch(arrayOf("*/*"))
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                    Text(
                        text = "Import from CSV",
                        fontSize = 18.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                    Text(
                        text = "Restore your data",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                }
            }
            Text(text = "⚠ Images are not included in .csv backups", fontSize = 14.sp, modifier = Modifier.offset(x = 30.dp).padding(vertical = 15.dp))
            Text(
                text = "Links",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(x = 30.dp)
            )
            Surface(
                modifier = Modifier.clickable {
                    uriHandler.openUri("https://github.com/ki-bun/Pioneer")
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                    Text(
                        text = "Source Code",
                        fontSize = 18.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                    Text(
                        text = "https://github.com/ki-bun/Pioneer",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                }
            }
            Surface(
                modifier = Modifier.clickable {
                    uriHandler.openUri("https://github.com/ki-bun/Pioneer/releases")
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                    Text(
                        text = "Changelog",
                        fontSize = 18.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                    Text(
                        text = "https://github.com/ki-bun/Pioneer/releases",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                }
            }
        }
    }
}