package com.expensey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.expensey.ui.theme.ExpenseyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // Create a NavController

            ExpenseyTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    bottomBar = {

                    }
                ) { innerPadding ->
                    // Create a NavHost to host the fragments
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route, // Set the starting destination
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            Greeting("Shashank!!!")
                        }
                        composable(Screen.Accounts.route) {
                            Text("Accounts Screen")
                        }
                        composable(Screen.Settings.route) {
                            Text("Settings Screen")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseyTheme {
        Greeting("Android")
    }
}

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Accounts : Screen("accounts", "Accounts")
    object Settings : Screen("settings", "Settings")
}
