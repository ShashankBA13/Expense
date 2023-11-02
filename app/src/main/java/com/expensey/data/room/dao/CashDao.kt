package com.expensey.data.room.dao

import androidx.room.Dao
import com.expensey.data.models.Cash

/**
 * [ROOM] DAO for [Cash] related operations
 */
@Dao
abstract class CashDao : BaseDao<Cash> {
}