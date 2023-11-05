package com.expensey.ui.screens.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.Cash
import com.expensey.data.repository.CashRepository
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application) : AndroidViewModel(application) {

	private val cashRepository : CashRepository
	val cashLiveData : LiveData<Cash>

	init {
		val expenseyApplication = application as ExpenseyApplication
		val cashDao = expenseyApplication.database.cashDao()
		cashRepository = CashRepository(cashDao, expenseyApplication.baseContext)
		cashLiveData = cashRepository.cashLiveData
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
}