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

	@Query("""
		 	SELECT * FROM bank_account
 	""")
	abstract fun fetchAllBankAccounts() : LiveData<List<BankAccount>>

	@Query(""" 
			SELECT * FROM bank_account WHERE id = :accountId 
	""")
	abstract fun fetchBankAccountById(accountId : Int) : LiveData<BankAccount>

	@Query(""" 
			UPDATE bank_account SET current_balance = (current_balance - :amountToDeduct) WHERE id = :accountId 
	""")
	abstract suspend fun updateAccountBalanceById(accountId : Int, amountToDeduct : Double)

	@Query("""
			SELECT ROUND(SUM(current_balance), 3) FROM bank_account
	""")
	abstract fun getTotalBalance(): LiveData<Double>

	@Query("""
			SELECT * FROM bank_account WHERE name LIKE :query
	""")
	abstract fun searchBankAccountsByName(query: String): LiveData<List<BankAccount>>

	/**
	 * The method's task is to update/add back the amount deducted from the account balance
	 * in case of an expense delete or expense update
	 */
	@Query("""
			UPDATE bank_account SET current_balance = ( current_balance + :amountToAddBack) WHERE id = :accountId
	""")
	abstract suspend fun addBackAccountBalanceById(accountId: Int, amountToAddBack : Double)
}