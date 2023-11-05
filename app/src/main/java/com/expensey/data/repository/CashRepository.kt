package com.expensey.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.expensey.data.models.Cash
import com.expensey.data.room.dao.CashDao

class CashRepository(private val cashDao : CashDao, private val context : Context) {

	val cashLiveData : LiveData<Cash> = cashDao.fetchAllCash()

	suspend fun insertCash(cash : Cash) {
		cashDao.insert(cash)
	}

	suspend fun updateCash(cash : Cash) {
		cashDao.update(cash)
	}
}