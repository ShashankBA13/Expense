package com.expensey.ui.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography

@Composable
fun AccountsScreen() {
    val viewModel: AccountsViewModel = viewModel()

    val assetsValue = 10000
    val liabilitiesValue = 20000
    val netWorth = assetsValue - liabilitiesValue

    val textColor = if (netWorth < 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Accounts",
                    modifier = Modifier
                        .padding(20.dp)
                        .weight(1f),
                    style = Typography.headlineLarge
                )
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Hamburger Icon",
                    modifier = Modifier.clickable {
                        // Handle the icon click here
                    } then Modifier
                        .padding(end = 20.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
            ) {
                Row (
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(70.dp)
                       .background(MaterialTheme.colorScheme.secondaryContainer),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Assets",
                            style = Typography.bodyLarge,
                            modifier = Modifier
                                .padding(top= 10.dp, start = 10.dp)
                        )
                        Text (
                            text = assetsValue.toString(),

                            style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp)
                        )
                    }
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Liabilities",
                            modifier = Modifier
                                .padding(top= 10.dp, start = 10.dp),
                            style = Typography.bodyLarge
                        )
                        Text (
                            text = liabilitiesValue.toString(),
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp),
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        )
                    }

                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Net Worth",
                            modifier = Modifier
                                .padding(top= 10.dp, start = 10.dp, end = 10.dp),
                            style = Typography.bodyLarge
                        )
                        Text (
                            text = netWorth.toString(),
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 10.dp),
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = textColor
                            )
                        )
                    }
                }
            }

            Cash()
            Accounts()
            CreditCards()
        }
    }
}

@Preview
@Composable
fun AccountsScreenPreview() {
    ExpenseyTheme {
        AccountsScreen()
    }
}

@Composable
fun Cash() {
    val viewModel: AccountsViewModel = viewModel()
    val cashLiveData by viewModel.cashLiveData.observeAsState()

    var text by remember { mutableStateOf(cashLiveData?.amount?.toString() ?: "") }
    if (cashLiveData != null) {
        text = cashLiveData !!.amount.toString()
    }

    Column {
        Row (
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp)
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = "Cash",
                modifier = Modifier.padding(10.dp),
                style = Typography.bodyLarge
            )
            Text (
                text = text,
                modifier = Modifier.padding(10.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
            )
        }
    }
}

@Composable
fun Accounts() {
    val cash = 0

    Column {
        Row (
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp)
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = "Accounts",
                modifier = Modifier.padding(10.dp),
                style = Typography.bodyLarge
            )
            Text (
                text = cash.toString(),
                modifier = Modifier.padding(10.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
            )
        }
    }
}

@Composable
fun CreditCards() {
    val totalLimit = 1000
    val availableLimit = 0
    val totalPayable = totalLimit - availableLimit

    Column {
        Row(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(20.dp)
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(5.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Labels and numbers wrapped in Columns
            Column {
                Text(
                    text = "Credit Cards",
                    modifier = Modifier.padding(10.dp),
                    style = Typography.bodyLarge
                )
                Text(
                    text = "Total Limit",
                    modifier = Modifier.padding(10.dp),
                    style = Typography.bodyMedium
                )
                Text(
                    text = "Available Limit",
                    modifier = Modifier.padding(10.dp),
                    style = Typography.bodyMedium
                )
                Text(
                    text = "Total Payable",
                    modifier = Modifier.padding(10.dp),
                    style = Typography.bodyMedium
                )
            }
            Column {
                Text(
                    text = "",
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = totalLimit.toString(),
                    modifier = Modifier.padding(10.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                )
                Text(
                    text = availableLimit.toString(),
                    modifier = Modifier.padding(10.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                )
                Text(
                    text = totalPayable.toString(),
                    modifier = Modifier.padding(10.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                )
            }
        }
    }
}
