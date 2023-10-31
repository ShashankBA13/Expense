package com.expensey.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        iconFilled = Icons.Filled.Home,
        iconOutlined = Icons.Outlined.Home
    )

    object Accounts : BottomBarScreen(
        route = "accounts",
        title = "Accounts",
        iconFilled = Icons.Filled.AccountBalance,
        iconOutlined = Icons.Outlined.AccountBalance
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        iconFilled = Icons.Filled.Settings,
        iconOutlined = Icons.Outlined.Settings
    )
}
