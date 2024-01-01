package com.expensey.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.expensey.data.models.BankAccount
import com.expensey.data.room.dao.BankAccountDao

class BankAccountRepository(private val bankAccountDao : BankAccountDao, private val context : Context) {

	val bankAccountLiveDataList : LiveData<List<BankAccount>> = bankAccountDao.fetchAllBankAccounts()

	val totalBalance : LiveData<Double> = bankAccountDao.getTotalBalance()
	suspend fun insertBankAccount(bankAccount : BankAccount) {
		bankAccountDao.insert(bankAccount)
	}

	suspend fun updateBankAccount(bankAccount : BankAccount) {
		bankAccountDao.update(bankAccount)
	}

	suspend fun deleteBankAccount(bankAccount : BankAccount) {
		bankAccountDao.delete(bankAccount)
	}

	fun fetchBankAccountById(accountId : Int) :  LiveData<BankAccount>{
		return bankAccountDao.fetchBankAccountById(accountId)
	}

	suspend fun updateAccountBalanceById(accountId : Int, currentBalance : Double) : LiveData<BankAccount> {
		bankAccountDao.updateAccountBalanceById(accountId, currentBalance)
		return fetchBankAccountById(accountId)
	}

	fun searchBankAccountsByName(query : String) : LiveData<List<BankAccount>> {
		return bankAccountDao.searchBankAccountsByName(query)
	}

	suspend fun addBackAccountBalanceById(accountId: Int, amountToAddBack : Double) {
		return bankAccountDao.addBackAccountBalanceById(accountId, amountToAddBack)
	}
}