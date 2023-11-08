package com.expensey.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.expensey.ui.screens.accounts.AccountsScreen
import com.expensey.ui.screens.accounts.config.AccountsConfiguration
import com.expensey.ui.screens.accounts.config.screens.BankAccountsScreen
import com.expensey.ui.screens.accounts.config.screens.CashScreen
import com.expensey.ui.screens.category.CategoryScreen
import com.expensey.ui.screens.expense.ExpenseScreen
import com.expensey.ui.screens.home.HomeScreen
import com.expensey.ui.screens.settings.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController)
        }

        composable(route = BottomBarScreen.Accounts.route) {
            AccountsScreen()
        }

        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(navController)
        }

        composable("category") {
            CategoryScreen(navController)
        }


        composable("accountsConfiguration") {
            AccountsConfiguration(navController = navController)
        }

        composable("cash") {
            CashScreen(navController)
        }

        composable("bankAccount/{bankAccountId}") { navBackStackEntry ->
            val bankAccountId : String? = navBackStackEntry.arguments?.getString("bankAccountId")

            if (bankAccountId != null) {
                Log.d("BottomNavGrap", bankAccountId)
                BankAccountsScreen(navController, bankAccountId.toInt())
            }
        }

        composable("creditCard") {
            TODO("Provide the navigation to Credit Card Screen here!!!")
        }
        
        composable("expense") {
            ExpenseScreen(navHostController = navController)
        }
    }
}