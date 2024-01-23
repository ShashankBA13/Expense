package com.expensey.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.expensey.R
import com.expensey.ui.theme.Typography
import com.expensey.util.DatabaseBackup

@Composable
fun SettingsScreen(navController : NavHostController) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Settings",
                style = Typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.archivo_black_regular)),

            )

            Row (
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    modifier = Modifier.clickable {
                        navController.navigate("category")
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = "Edit Category", // Provide a content description as needed
                        modifier = Modifier
                            .size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Category",
                        modifier = Modifier
                            .padding(16.dp, 5.dp),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    modifier = Modifier.clickable {
                        navController.navigate("accountsConfiguration")
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalance,
                        contentDescription = "Edit Account",
                        modifier = Modifier
                            .size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Accounts",
                        modifier = Modifier
                            .padding(16.dp, 5.dp),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    modifier = Modifier.clickable {
                        // Calling the function to perform a local backup of the DB
                        DatabaseBackup.backupDatabase(context, "expensey_database")
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Backup,
                        contentDescription = "Backup Data",
                        modifier = Modifier
                            .size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Local Backup",
                        modifier = Modifier
                            .padding(16.dp, 5.dp),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}