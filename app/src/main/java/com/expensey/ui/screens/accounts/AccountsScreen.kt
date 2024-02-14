package com.expensey.ui.screens.accounts

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.expensey.R
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography
import java.text.NumberFormat

@Composable
fun AccountsScreen(navController: NavController) {
    val viewModel: AccountsViewModel = viewModel()
    val context = LocalContext.current

    // Cash related Data
    val cashLiveData by viewModel.cashLiveData.observeAsState()
    var cash by remember { mutableStateOf(cashLiveData?.amount?.toString() ?: "") }
    if (cashLiveData != null) {
        cash = cashLiveData!!.amount.toString()
    }

    // BankAccounts related Data
    val totalBalanceLiveData by viewModel.totalBalance.observeAsState()
    var totalBankBalance by remember { mutableStateOf(totalBalanceLiveData?.toString() ?: "") }
    if (totalBalanceLiveData != null) {
        totalBankBalance = totalBalanceLiveData.toString()
    }

    //Credit Card Related Data
    val totalNoOfCreditCards by viewModel.totalNoOfCreditCard().observeAsState()
    var totalCreditCards by remember { mutableStateOf(totalNoOfCreditCards?.toString() ?: "") }
    if (totalNoOfCreditCards != null) {
        totalCreditCards = totalNoOfCreditCards.toString()
    }

    val totalLimitLiveData by viewModel.totalLimit().observeAsState()
    var totalLimit by remember {
        mutableStateOf(totalLimitLiveData?.toString() ?: "")
    }
    if (totalLimitLiveData != null) {
        totalLimit = totalLimitLiveData.toString()
    }

    val totalAvailableLimitLiveData by viewModel.totalRemainingBalance().observeAsState()
    var totalAvailableLimit by remember {
        mutableStateOf(totalAvailableLimitLiveData?.toString() ?: "")
    }
    if (totalAvailableLimitLiveData != null) {
        totalAvailableLimit = totalAvailableLimitLiveData.toString()
    }

    val balancePayable: Double = if (totalLimit != "" && totalAvailableLimit != "") {
        totalLimit.toDouble() - totalAvailableLimit.toDouble()
    } else {
        0.0
    }

    val assetsValue: Double = when {
        cash.isNotBlank() -> {
            if (totalBankBalance.isNotBlank()) {
                cash.toDouble() + totalBankBalance.toDouble()
            } else {
                cash.toDouble()
            }
        }

        totalBankBalance.isNotBlank() -> totalBankBalance.toDouble()
        else -> 0.0
    }

    val netWorth: Double = assetsValue - balancePayable
    val formattedNetWorth = formatNumber(number = netWorth)
    "%.2f".format(balancePayable)


    val textColor =
        if (netWorth < 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary

    val assetsValueString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 20.sp)) {
            append("₹")
        }
        withStyle(
            style = SpanStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append(formatNumber(number = assetsValue))
        }
    }

    val liabilitiesValueString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary
            )
        ) {
            append("₹")
        }
        withStyle(
            style = SpanStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.secondary
            )
        ) {
            append(formatNumber(number = balancePayable))
        }
    }

    val netWorthValueString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 20.sp, color = textColor)) {
            append("₹")
        }
        withStyle(
            style = SpanStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = textColor
            )
        ) {
            append(" $formattedNetWorth")
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Accounts",
                    style = Typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(R.font.archivo_black_regular))
                )
                HamBurgerMenu(
                    icon = Icons.Outlined.MoreVert,
                    contentDescription = "Hamburger Icon",
                    items = listOf("Item 1", "Item 2", "Item 3"),
                    navController = navController
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Assets",
                            style = Typography.bodyLarge,
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp)
                        )
                        Text(
                            text = assetsValueString, style = TextStyle(
                                fontSize = 30.sp,
                                fontFamily = FontFamily(Font(R.font.archivo_black_regular)),
                                color = MaterialTheme.colorScheme.primary
                            ), modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Liabilities",
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                            style = Typography.bodyLarge
                        )
                        Text(
                            text = liabilitiesValueString,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp),
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontFamily = FontFamily(Font(R.font.archivo_black_regular)),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Net Worth",
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
                            style = Typography.bodyLarge
                        )
                        Text(
                            text = netWorthValueString,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 10.dp),
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontFamily = FontFamily(Font(R.font.archivo_black_regular)),
                                color = textColor
                            )
                        )
                    }
                }
            }

            Cash()
            BankAccount()
            CreditCards()
        }
    }
}

@Composable
fun Cash() {
    val viewModel: AccountsViewModel = viewModel()
    val cashLiveData by viewModel.cashLiveData.observeAsState()

    var text by remember { mutableStateOf(cashLiveData?.amount?.toString() ?: "0.0") }
    if (cashLiveData != null) {
        text = cashLiveData!!.amount.toString()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cash", style = Typography.titleLarge

            )
            Text(
                text = "₹ ${formatNumber(text.toDouble())}", style = Typography.headlineSmall
            )
        }
    }
}

@Composable
fun BankAccount() {
    val viewModel: AccountsViewModel = viewModel()
    val totalBalance by viewModel.totalBalance.observeAsState()

    var text by remember { mutableStateOf(totalBalance?.toString() ?: " ₹  0.0") }
    if (totalBalance != null) {
        text = formatNumber(totalBalance!!.toDouble())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Accounts",
                style = Typography.titleLarge
            )
            Text(
                text = "₹ $text",
                style = Typography.headlineSmall,
            )
        }
    }
}

@Composable
fun CreditCards() {
    val viewModel: AccountsViewModel = viewModel()
    val totalNoOfCreditCards by viewModel.totalNoOfCreditCard().observeAsState()
    var totalCreditCards by remember { mutableStateOf(totalNoOfCreditCards?.toString() ?: "") }
    if (totalNoOfCreditCards != null) {
        totalCreditCards = totalNoOfCreditCards.toString()
    }

    val totalLimitLiveData by viewModel.totalLimit().observeAsState()
    var totalLimit by remember { mutableStateOf(totalLimitLiveData?.toString() ?: "0.0") }
    if (totalLimitLiveData != null) {
        totalLimit = totalLimitLiveData.toString()
    }

    val totalAvailableLimitLiveData by viewModel.totalRemainingBalance().observeAsState()
    var totalAvailableLimit by remember {
        mutableStateOf(totalAvailableLimitLiveData?.toString() ?: "0.0")
    }
    if (totalAvailableLimitLiveData != null) {
        totalAvailableLimit = totalAvailableLimitLiveData.toString()
    }

    val balancePayable : Double = if (totalLimit != "" && totalAvailableLimit != "") {
        totalLimit.toDouble() - totalAvailableLimit.toDouble()
    } else {
        0.0
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Credit Cards",
                style = Typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Across $totalCreditCards Cards", modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Limit", style = Typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "₹ ${formatNumber(totalLimit.toDouble())}", style = Typography.headlineSmall
                    )
                }
                Column {
                    Text(
                        text = "Available Limit", style = Typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "₹ ${formatNumber(totalAvailableLimit.toDouble())}", style = Typography.headlineSmall
                    )
                }
                Column {
                    Text(
                        text = "Total Payable", style = Typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "₹ ${formatNumber(balancePayable)}",
                        style = Typography.headlineSmall
                    )
                }
            }
        }
    }
}


@Composable
fun formatNumber(number: Double): String {
    val formatter = NumberFormat.getInstance()
    return formatter.format(number)
}

@Composable
fun HamBurgerMenu(
    icon: ImageVector,
    contentDescription: String,
    items: List<String>,
    navController: NavController
) {
    // State to track if dropdown menu is expanded or not
    val expanded = remember { mutableStateOf(false) }

    // State to keep track of the selected item
    val selectedItem = remember { mutableStateOf("") }

    Column {
        // Icon with click listener to show/hide dropdown menu
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.clickable {
                expanded.value = true
            }
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {

                DropdownMenuItem(text = { Text(text = "Configure Accounts") }, onClick = { navController.navigate("accountsConfiguration") })

        }
    }
}
