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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expensey.ui.theme.ExpenseyTheme
import com.expensey.ui.theme.Typography

@Composable
fun AccountsScreen() {
	val viewModel : AccountsViewModel = viewModel()

	// Cash Related Data
	val cashLiveData by viewModel.cashLiveData.observeAsState()

	var cash by remember { mutableStateOf(cashLiveData?.amount?.toString() ?: "") }
	if (cashLiveData != null) {
		cash = cashLiveData !!.amount.toString()
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

	val balancePayable : Double = if (totalLimit != "" && totalAvailableLimit != "") {
		totalLimit.toDouble() - totalAvailableLimit.toDouble()
	} else {
		0.0
	}

	val assetsValue : Double = if (cash != "" && totalBankBalance != "") {
		cash.toDouble() + totalBankBalance.toDouble()
	} else {
		0.0
	}

	val liabilitiesValue = balancePayable
	val netWorth : Double = assetsValue - liabilitiesValue
	val formattedNetWorth = "%.3f".format(netWorth)


	val textColor = if (netWorth < 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary

    val assetsValueString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 20.sp)) {
            append("₹")
        }
        withStyle(style = SpanStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)) {
            append(" $assetsValue")
        }
    }

    val liabilitiesValueString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 20.sp)) {
            append("₹")
        }
        withStyle(style = SpanStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)) {
            append(" $liabilitiesValue")
        }
    }

    val netWorthValueString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 20.sp)) {
            append("₹")
        }
        withStyle(style = SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)) {
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
					modifier = Modifier
						.weight(1f),
					style = Typography.headlineLarge
				)
				Icon(
					imageVector = Icons.Outlined.MoreVert,
					contentDescription = "Hamburger Icon",
					modifier = Modifier.clickable {
						// Handle the icon click here
					}
				)
			}

			Card(
				modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
			) {
				Row(
					modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .border(1.dp, MaterialTheme.colorScheme.primaryContainer),
					horizontalArrangement = Arrangement.SpaceAround,
				) {
					Column(
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "Assets",
							style = Typography.bodyLarge,
							modifier = Modifier
								.padding(top = 10.dp, start = 10.dp)
						)
						Text(
							text = assetsValueString,
							style = TextStyle(
								fontSize = 30.sp,
								fontWeight = FontWeight.Bold,
								fontFamily = FontFamily.Monospace,
								color = MaterialTheme.colorScheme.primary
							),
							modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp)
						)
					}
					Column(
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "Liabilities",
							modifier = Modifier
								.padding(top = 10.dp, start = 10.dp),
							style = Typography.bodyLarge
						)
						Text(
							text = liabilitiesValueString,
							modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 5.dp),
							style = TextStyle(
								fontSize = 30.sp,
								fontWeight = FontWeight.Bold,
								fontFamily = FontFamily.Monospace,
								color = MaterialTheme.colorScheme.tertiary
							)
						)
					}

					Column(
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "Net Worth",
							modifier = Modifier
								.padding(top = 10.dp, start = 10.dp, end = 10.dp),
							style = Typography.bodyLarge
						)
						Text(
							text = netWorthValueString,
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
			BankAccount()
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
	val viewModel : AccountsViewModel = viewModel()
	val cashLiveData by viewModel.cashLiveData.observeAsState()

	var text by remember { mutableStateOf(cashLiveData?.amount?.toString() ?: "") }
	if (cashLiveData != null) {
		text = cashLiveData !!.amount.toString()
	}

	Column {
		Row(
			modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(5.dp)
                ),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Cash",
				modifier = Modifier.padding(10.dp),
				style = Typography.bodyLarge
			)
			Text(
				text = "₹ $text",
				modifier = Modifier.padding(10.dp),
				style = TextStyle(
					fontSize = 20.sp,
					fontWeight = FontWeight.SemiBold,
					fontFamily = FontFamily.Monospace
				)
			)
		}
	}
}

@Composable
fun BankAccount() {
	val viewModel : AccountsViewModel = viewModel()

	val totalBalance by viewModel.totalBalance.observeAsState()

	var text by remember { mutableStateOf(totalBalance?.toString() ?: "") }
	if (totalBalance != null) {
		text = totalBalance !!.toString()
	}

	Column {
		Row(
			modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(20.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(5.dp)
                ),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Accounts",
				modifier = Modifier.padding(10.dp),
				style = Typography.bodyLarge
			)
			Text(
				text = "₹ " + totalBalance.toString(),
				modifier = Modifier.padding(10.dp),
				style = TextStyle(
					fontSize = 20.sp,
					fontWeight = FontWeight.SemiBold,
					fontFamily = FontFamily.Monospace
				)
			)
		}
	}
}

@Composable
fun CreditCards() {
	val viewModel : AccountsViewModel = viewModel()

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

	val balancePayable = if (totalLimit != "" && totalAvailableLimit != "") {
		totalLimit.toDouble() - totalAvailableLimit.toDouble()
	} else {
		""
	}

	Column(
		modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(5.dp)
            )
            .height(150.dp)
	) {
		Row (
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
			Text(
				text = "Credit Cards",
				style = Typography.bodyLarge,
				textAlign = TextAlign.Start
			)
			Text(
				text = "Across $totalCreditCards Cards",
				style = Typography.bodyLarge,
				color = Color.Gray,
                textAlign = TextAlign.End
			)
		}

        Row (
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )  {
			Text(
				text = "Total Limit",
				style = Typography.bodyMedium,
                textAlign = TextAlign.Start
			)
			Text(
				text = "₹ $totalLimit",
				style = TextStyle(
					fontSize = 20.sp,
					fontWeight = FontWeight.SemiBold,
					fontFamily = FontFamily.Monospace
				),
				modifier = Modifier.padding(end = 10.dp)
			)
		}
        Row (
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )  {
			Text(
				text = "Available Limit",
				style = Typography.bodyMedium,
                textAlign = TextAlign.Start
			)
			Text(
				text = "₹ $totalAvailableLimit",
				style = TextStyle(
					fontSize = 20.sp,
					fontWeight = FontWeight.SemiBold,
					fontFamily = FontFamily.Monospace
				),
				modifier = Modifier.padding(end = 10.dp)
			)
		}

        Row (
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )  {
			Text(
				text = "Total Payable",
				style = Typography.bodyMedium,
                textAlign = TextAlign.Start
			)
			Text(
				text = "₹ $balancePayable",
				style = TextStyle(
					fontSize = 20.sp,
					fontWeight = FontWeight.SemiBold,
					fontFamily = FontFamily.Monospace
				),
                modifier = Modifier.padding(end = 10.dp)
			)
		}
	}
}

