package com.expensey

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expensey.ui.navigation.BottomBarScreen
import com.expensey.ui.navigation.BottomNavGraph

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
	val navController = rememberNavController()

	// Create a list of BottomBarScreen items
	val screens = listOf(
		BottomBarScreen.Home,
		BottomBarScreen.Accounts,
		BottomBarScreen.Insights,
		BottomBarScreen.Settings
	)

	// Get the current destination from the navigation controller
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination

	Scaffold(
		bottomBar = {
			// Create the BottomNavigation composable
			if (currentDestination?.route == BottomBarScreen.Home.route ||
				currentDestination?.route == BottomBarScreen.Accounts.route ||
				currentDestination?.route == BottomBarScreen.Insights.route ||
				currentDestination?.route == BottomBarScreen.Settings.route
			) {
				BottomNavigation {
					// Loop through the screen items and create a BottomNavigationItem for each
					screens.forEach { screen ->
						BottomNavigationItem(
							icon = {
								// Use the current screen's icon, either filled or outlined
								val icon =
									if (currentDestination.hierarchy.any { it.route == screen.route }) {
										screen.iconFilled
									} else {
										screen.iconOutlined
									}
								Icon(
									imageVector = icon,
									contentDescription = null,
									tint = MaterialTheme.colorScheme.primary
								)
							},
							label = {
								Text(
									text = screen.title,
									color = MaterialTheme.colorScheme.primary
								)
							}
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
		}
	) {
		// Display the content of the selected screen
		BottomNavGraph(navController = navController)
	}
}

@Composable
fun BottomNavigationItem(
	icon : @Composable () -> Unit,
	label : @Composable () -> Unit,
	onClick : () -> Unit
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		IconButton(onClick = onClick) {
			icon()
		}
//		label()
	}
}

@Composable
fun BottomNavigation(
	content : @Composable () -> Unit,
) {

	Box(
		modifier = Modifier
            .fillMaxWidth()
			.background(NavigationBarDefaults.containerColor),

	) {
		Row(
			horizontalArrangement = Arrangement.SpaceEvenly,
			modifier = Modifier
                .fillMaxWidth()
                .background(NavigationBarDefaults.containerColor)
		) {
			content()
		}
	}
}
