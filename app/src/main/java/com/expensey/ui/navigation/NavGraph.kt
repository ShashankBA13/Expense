package com.expensey.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.expensey.ui.screens.accounts.AccountsScreen
import com.expensey.ui.screens.accounts.config.AccountsConfiguration
import com.expensey.ui.screens.accounts.config.screens.BankAccountsScreen
import com.expensey.ui.screens.accounts.config.screens.CashScreen
import com.expensey.ui.screens.accounts.config.screens.CreditCardScreen
import com.expensey.ui.screens.category.CategoryScreen
import com.expensey.ui.screens.expense.ExpenseScreen
import com.expensey.ui.screens.home.HomeScreen
import com.expensey.ui.screens.insights.InsightsScreen
import com.expensey.ui.screens.settings.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
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
            AccountsScreen(navController)
        }

        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(navController)
        }

        composable(route = BottomBarScreen.Insights.route) {
            InsightsScreen(navController)
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
                BankAccountsScreen(navController, bankAccountId.toInt())
            }
        }

        composable("creditCard/{creditCardId}") {navBackStackEntry ->
            val creditCardId : String? = navBackStackEntry.arguments?.getString("creditCardId")

            if (creditCardId != null) {
                CreditCardScreen(navController, creditCardId.toInt())
            }
        }
        
        composable("expense/{expenseId}") {navBackStackEntry ->
            val expenseId : String? = navBackStackEntry.arguments?.getString("expenseId")

            if (expenseId != null) {
                ExpenseScreen(navHostController = navController, expenseId.toInt())
            }
        }
    }
}