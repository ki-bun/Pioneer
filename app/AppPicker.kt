@Composable
fun AppList() {
    val context = LocalContext.current
    val pm = context.packageManager

    val apps = remember {
        pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
            .filter {
                it.flags and ApplicationInfo.FLAG_SYSTEM == 0
            }
            .sortedBy {
                pm.getApplicationLabel(it).toString().lowercase()
            }
    }

    LazyColumn {
        items(apps, key = { it.packageName }) { app ->
            Text(
                text = pm.getApplicationLabel(app).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // You already have the package name here!
                        val packageName = app.packageName
                        // Save it, navigate back, etc.
                    }
                    .padding(16.dp)
            )
        }
    }
}