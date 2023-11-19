package com.expensey.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.expensey.data.models.CreditCard
import com.expensey.data.room.dao.CreditCardDao

class CreditCardRepository(private val creditCardDao : CreditCardDao, private val context : Context) {

	suspend fun insertCreditCard(creditCard : CreditCard) {
		creditCardDao.insert(creditCard)
	}

	suspend fun updateCreditCard(creditCard : CreditCard) {
		creditCardDao.update(creditCard)
	}

	suspend fun deleteCreditCard(creditCard : CreditCard) {
		creditCardDao.delete(creditCard)
	}

	fun fetchCreditCardById(creditCardId : Int) : LiveData<CreditCard> {
		return creditCardDao.getCreditCardById(creditCardId)
	}

	fun updateCreditCardTotalLimitById(creditCardId : Int, totalLimit : Double) : LiveData<CreditCard> {
		creditCardDao.updateCurrentBalanceById(creditCardId, totalLimit)
		return fetchCreditCardById(creditCardId)
	}

	fun updateCurrentBalanceById(creditCardId : Int, currentBalance : Double) : LiveData<CreditCard> {
		creditCardDao.updateCurrentBalanceById(creditCardId, currentBalance)
		return fetchCreditCardById(creditCardId)
	}
}