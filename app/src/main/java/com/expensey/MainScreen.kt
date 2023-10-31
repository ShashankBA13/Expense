package com.expensey

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expensey.ui.navigation.BottomBarScreen
import com.expensey.ui.navigation.BottomNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Create a list of BottomBarScreen items
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Accounts,
        BottomBarScreen.Settings,
    )

    // Get the current destination from the navigation controller
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            // Create the BottomNavigation composable
            BottomNavigation {
                // Loop through the screen items and create a BottomNavigationItem for each
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            // Use the current screen's icon, either filled or outlined
                            val icon = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                screen.iconFilled
                            } else {
                                screen.iconOutlined
                            }
                            Icon(imageVector = icon, contentDescription = null)
                        },
                        label = { Text(text = screen.title) }
                    ) {
                        // Navigate to the selected screen's route
                        navController.navigate(screen.route) {
                            // Pop up to the start destination when reselecting the current tab
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected tab
                            restoreState = true
                        }
                    }
                }
            }
        }
    ) {
        // Display the content of the selected screen
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomNavigationItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onClick: () -> Unit
) {
    // You can define the layout and behavior for a single BottomNavigationItem here
    // For example, you can create a clickable area with the icon and label.

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onClick) {
            icon()
        }
        // Always display the label (screen name) below the icon
        label()
    }
}




@Composable
fun BottomNavigation(
    content: @Composable () -> Unit) {
    // You can define the layout and behavior for the entire BottomNavigation here.
    // This may include rendering the individual BottomNavigationItems and any additional logic.
    // For example, you can use a Row to arrange the items horizontally.
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}
