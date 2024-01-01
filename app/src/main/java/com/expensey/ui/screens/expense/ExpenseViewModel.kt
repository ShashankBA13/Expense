package com.expensey.ui.screens.expense

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

class ExpenseViewModel(application : Application) : AndroidViewModel(application) {

	private val bankAccountRepository : BankAccountRepository
	private val cashRepository : CashRepository
	private val creditCardRepository : CreditCardRepository

	val cashLiveData : LiveData<Cash>

	val bankAccountLiveDataList : LiveData<List<BankAccount>>

	val creditCardLiveDataList : LiveData<List<CreditCard>>

	init {
		val expenseyApplication = application as ExpenseyApplication

		val bankAccountDao = expenseyApplication.database.bankAccountDao()
		bankAccountRepository = BankAccountRepository(bankAccountDao, expenseyApplication.baseContext)

		val cashDao = expenseyApplication.database.cashDao()
		cashRepository = CashRepository(cashDao, expenseyApplication.baseContext)

		val creditCardDao = expenseyApplication.database.creditCardDao()
		creditCardRepository = CreditCardRepository(creditCardDao, expenseyApplication.baseContext)

		cashLiveData = cashRepository.cashLiveData
		bankAccountLiveDataList = bankAccountRepository.bankAccountLiveDataList
		creditCardLiveDataList = creditCardRepository.creditCardLiveDataList
	}

	fun addBackAccountBalanceById(accountId: Int, amountToAddBack : Double) {
		Log.d("ExpenseViewMode", "Inside view model")
		viewModelScope.launch {
			bankAccountRepository.addBackAccountBalanceById(accountId, amountToAddBack)
		}
	}
}