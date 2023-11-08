package com.expensey.ui.screens.accounts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.BankAccount
import com.expensey.data.models.Cash
import com.expensey.data.repository.BankAccountRepository
import com.expensey.data.repository.CashRepository
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application) : AndroidViewModel(application) {

	private val cashRepository : CashRepository
	private val bankAccountRepository : BankAccountRepository
	private val TAG = "AccountsViewModel"

	val cashLiveData : LiveData<Cash>

	val totalBalance: LiveData<Double>

	val bankAccountLiveDataList : LiveData<List<BankAccount>>

	init {
		val expenseyApplication = application as ExpenseyApplication

		val cashDao = expenseyApplication.database.cashDao()
		val bankAccountDao = expenseyApplication.database.bankAccountDao()

		cashRepository = CashRepository(cashDao, expenseyApplication.baseContext)
		bankAccountRepository = BankAccountRepository(bankAccountDao, expenseyApplication.baseContext)

		cashLiveData = cashRepository.cashLiveData
		totalBalance = bankAccountRepository.totalBalance
		bankAccountLiveDataList = bankAccountRepository.bankAccountLiveDataList
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
		Log.d(TAG, bankAccountRepository.fetchBankAccountById(accountId).value.toString())
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
}