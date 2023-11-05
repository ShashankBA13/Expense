package com.expensey.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.expensey.ui.theme.Typography

@Composable
fun SettingsScreen(navController : NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {

            Text (
                text = "Settings",
                modifier = Modifier.padding(20.dp),
                style = Typography.headlineLarge
            )

            Row {
                Column (
                    modifier = Modifier.clickable {
                        navController.navigate("category")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = "Edit Category", // Provide a content description as needed
                        modifier = Modifier
                            .padding(start = 20.dp, end = 0.dp, top = 20.dp, bottom = 5.dp)
                            .size(40.dp)
                    )
                    Text(
                        text = "Category",
                        modifier = Modifier
                            .padding(16.dp, 0.dp),
                        style = Typography.bodyMedium
                    )
                }

                Column (
                    modifier = Modifier.clickable {
                        navController.navigate("accountsConfiguration")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalance,
                        contentDescription = "Edit Category", // Provide a content description as needed
                        modifier = Modifier
                            .padding(start = 20.dp, end = 0.dp, top = 20.dp, bottom = 5.dp)
                            .size(40.dp)
                    )
                    Text(
                        text = "Accounts",
                        modifier = Modifier
                            .padding(16.dp, 0.dp),
                        style = Typography.bodyMedium
                    )
                }
            }
        }
    }
}