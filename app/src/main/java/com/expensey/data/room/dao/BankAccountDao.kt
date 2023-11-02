package com.expensey.data.room.dao

import androidx.room.Dao
import com.expensey.data.models.BankAccount

/**
 * [ROOM] DAO for [BankAccount] related operations
 */
@Dao
abstract class BankAccountDao : BaseDao<BankAccount> {
}