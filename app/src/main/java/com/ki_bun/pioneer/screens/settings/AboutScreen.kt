package com.ki_bun.pioneer.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ) {
            Text(
                text = "App Information",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(x = 30.dp)
            )
            Surface(modifier = Modifier.clickable {
                uriHandler.openUri("https://github.com/ki-bun")
            }) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                    Text(
                        text = "Author",
                        fontSize = 18.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                    Text(
                        text = "ki-bun",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.offset(x = 30.dp)
                    )
                }
            }

            Surface(modifier = Modifier.clickable {
                uriHandler.openUri("https://github.com/ki-bun/Pioneer")
            }) {
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

            Surface(modifier = Modifier.clickable {
                uriHandler.openUri("https://github.com/ki-bun/Pioneer/releases")
            }) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
                    Text(
                        text = "Releases",
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