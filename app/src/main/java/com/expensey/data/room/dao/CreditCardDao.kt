package com.expensey.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.expensey.data.models.CreditCard

/**
 * [ROOM] DAO for [CreditCard] related operations
 */
@Dao
abstract class CreditCardDao : BaseDao<CreditCard> {

	@Query(""" 
			SELECT * FROM credit_card 
	""")
	abstract fun fetchAllCreditCards() : LiveData<List<CreditCard>>

	@Query("""
			SELECT * FROM credit_card WHERE id = :creditCardId 
	""")
	abstract fun getCreditCardById(creditCardId : Int) : LiveData<CreditCard>

	@Query("""
			UPDATE credit_card SET total_limit = :totalLimit WHERE id = :creditCardId
	""")
	abstract fun updateCreditCardTotalLimitById(creditCardId : Int, totalLimit : Double)

	@Query("""
			UPDATE credit_card SET current_balance = (current_balance - :currentBalance)  WHERE id = :creditCardId
	""")
	abstract suspend fun updateCurrentBalanceById(creditCardId : Int, currentBalance : Double)

	@Query(""" 
			SELECT * FROM credit_card WHERE name LIKE :query 
	""")
	abstract fun searchCreditCardsByName(query : String) : LiveData<List<CreditCard>>

	@Query("""
		 	SELECT ROUND(SUM(total_limit), 3) FROM credit_card 
	""")
	abstract fun totalLimit() : LiveData<Double>

	@Query("""
			SELECT ROUND(SUM(current_balance), 3) FROM credit_card 
	""")
	abstract fun totalRemainingBalance() : LiveData<Double>

	@Query("""
			SELECT COUNT(*) FROM credit_card 
	""")
	abstract fun totalNoOfCreditCard() : LiveData<Int>

	@Query("""
			UPDATE credit_card SET current_balance = ( current_balance + :amountToAddBack) WHERE id = :creditCardId
	""")
	abstract suspend fun addBackDeductedAmount(creditCardId: Int, amountToAddBack : Double)
}