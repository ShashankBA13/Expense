package com.expensey.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.expensey.data.models.Cash

/**
 * [ROOM] DAO for [Cash] related operations
 */
@Dao
abstract class CashDao : BaseDao<Cash> {

	@Query(" SELECT * FROM cash LIMIT 1")
	abstract fun fetchAllCash() : LiveData<Cash>
}