package com.expensey.ui.screens.accounts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.BankAccount
import com.expensey.data.models.Cash
import com.expensey.data.models.CreditCard
import com.expensey.data.repository.BankAccountRepository
import com.expensey.data.repository.CashRepository
import com.expensey.data.repository.CreditCardRepository
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application) : AndroidViewModel(application) {

	private val cashRepository : CashRepository
	private val bankAccountRepository : BankAccountRepository
	private val creditCardRepository : CreditCardRepository
	private val TAG = "AccountsViewModel"

	val cashLiveData : LiveData<Cash>

	val totalBalance: LiveData<Double>

	val bankAccountLiveDataList : LiveData<List<BankAccount>>

	val creditCardLiveDataList : LiveData<List<CreditCard>>

	init {
		val expenseyApplication = application as ExpenseyApplication

		val cashDao = expenseyApplication.database.cashDao()
		val bankAccountDao = expenseyApplication.database.bankAccountDao()
		val creditCardDao = expenseyApplication.database.creditCardDao()

		cashRepository = CashRepository(cashDao, expenseyApplication.baseContext)
		bankAccountRepository = BankAccountRepository(bankAccountDao, expenseyApplication.baseContext)
		creditCardRepository = CreditCardRepository(creditCardDao, expenseyApplication.baseContext)

		cashLiveData = cashRepository.cashLiveData
		totalBalance = bankAccountRepository.totalBalance
		bankAccountLiveDataList = bankAccountRepository.bankAccountLiveDataList
		creditCardLiveDataList = creditCardRepository.creditCardLiveDataList
	}

	fun insertCash(cash : Cash) {
		viewModelScope.launch {
			cashRepository.insertCash(cash)
		}
	}

	fun updateCash(cash : Cash) {
		viewModelScope.launch {
			cashRepository.updateCash(cash)
		}
	}

	fun insertBankAccount(bankAccount: BankAccount) {
		viewModelScope.launch {
			bankAccountRepository.insertBankAccount(bankAccount)
		}
	}

	fun updateBankAccount(bankAccount: BankAccount) {
		viewModelScope.launch {
			bankAccountRepository.updateBankAccount(bankAccount)
		}
	}

	fun deleteBankAccount(bankAccount: BankAccount) {
		viewModelScope.launch {
			bankAccountRepository.deleteBankAccount(bankAccount)
		}
	}

	fun getBankAccountById(accountId: Int): LiveData<BankAccount> {
		return bankAccountRepository.fetchBankAccountById(accountId)
	}

	fun updateAccountBalanceById(accountId: Int, currentBalance: Double) {
		viewModelScope.launch {
			bankAccountRepository.updateAccountBalanceById(accountId, currentBalance)
		}
	}

	fun searchBankAccountsByName(query: String): LiveData<List<BankAccount>> {
		return bankAccountRepository.searchBankAccountsByName(query)
	}

	fun insertCreditCard(creditCard : CreditCard) {
		viewModelScope.launch {
			creditCardRepository.insertCreditCard(creditCard)
		}
	}

	fun updateCreditCard(creditCard : CreditCard) {
		viewModelScope.launch {
			creditCardRepository.updateCreditCard(creditCard)
		}
	}

	fun deleteCreditCard(creditCard : CreditCard) {
		viewModelScope.launch {
			creditCardRepository.deleteCreditCard(creditCard)
		}
	}

	fun fetchCreditCardById(creditCardId : Int) : LiveData<CreditCard> {
		return creditCardRepository.fetchCreditCardById(creditCardId)
	}

	fun totalNoOfCreditCard() : LiveData<Int> {
		return creditCardRepository.totalNoOfCreditCard()
	}

	fun totalLimit() : LiveData<Double> {
		return creditCardRepository.totalLimit()
	}

	fun totalRemainingBalance() : LiveData<Double> {
		return creditCardRepository.totalRemainingBalance()
	}

	fun updateCreditCardTotalLimitById(creditCardId : Int, totalLimit : Double) : LiveData<CreditCard> {
		viewModelScope.launch {
			creditCardRepository.updateCreditCardTotalLimitById(creditCardId, totalLimit)
		}
		return fetchCreditCardById(creditCardId)
	}

	fun updateCurrentBalanceById(creditCardId : Int, currentBalance : Double) : LiveData<CreditCard> {
		viewModelScope.launch {
			creditCardRepository.updateCurrentBalanceById(creditCardId, currentBalance)
		}
		return fetchCreditCardById(creditCardId)
	}

	fun addBackDedcutedAmountInCreditCardById(creditCardId: Int, amounToAddBack: Double) {
		viewModelScope.launch {
			creditCardRepository.addBackDedcutedAmountInCreditCardById(creditCardId, amounToAddBack)
		}
	}
}