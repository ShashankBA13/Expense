package com.expensey.data.room.dao

import androidx.room.Dao
import com.expensey.data.models.CreditCard

/**
 * [ROOM] DAO for [CreditCard] related operations
 */
@Dao
abstract class CreditCardDao : BaseDao<CreditCard> {
}