package com.expensey.data.models

data class FinancialInfo(
	val cashList : List<Cash>,
	val bankAccountList : List<BankAccount>,
	val creditCardList : List<CreditCard>
)
