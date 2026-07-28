package com.ki_bun.pioneer.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ki_bun.pioneer.R
import com.ki_bun.pioneer.viewmodel.ProgressViewModel

@Composable
fun BackupScreen(navController: NavController,progressViewModel: ProgressViewModel) {
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
                Text(text = "Backup", fontSize = 24.sp, modifier = Modifier.padding(start = 5.dp))
            }

            Text(
                text = "Manual Backup",
                fontWeight = FontWeight.Bold,
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
            Text(
                text = "⚠ Images are not included in .csv backups",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .offset(x = 30.dp)
                    .padding(vertical = 15.dp)
            )
        }
    }
}