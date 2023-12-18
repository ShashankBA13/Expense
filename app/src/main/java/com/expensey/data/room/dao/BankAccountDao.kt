package com.expensey.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.expensey.data.models.BankAccount

/**
 * [ROOM] DAO for [BankAccount] related operations
 */
@Dao
abstract class BankAccountDao : BaseDao<BankAccount> {

	@Query(" SELECT * FROM bank_account")
	abstract fun fetchAllBankAccounts() : LiveData<List<BankAccount>>

	@Query(" SELECT * FROM bank_account WHERE id = :accountId ")
	abstract fun fetchBankAccountById(accountId : Int) : LiveData<BankAccount>

	@Query(""" 
			UPDATE bank_account SET current_balance = (current_balance - :currentBalance) WHERE id = :accountId 
			"""
	)
	abstract suspend fun updateAccountBalanceById(accountId : Int, currentBalance : Double)

	@Query("SELECT ROUND(SUM(current_balance), 3) FROM bank_account")
	abstract fun getTotalBalance(): LiveData<Double>

	@Query("SELECT * FROM bank_account WHERE name LIKE :query")
	abstract fun searchBankAccountsByName(query: String): LiveData<List<BankAccount>>
}