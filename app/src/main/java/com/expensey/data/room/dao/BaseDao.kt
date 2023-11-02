package com.expensey.data.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base DAO that includes all necessary [CRUD] functions
 */
interface BaseDao<T> {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(entity: T): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAll(vararg entity: T)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAll(entities: Collection<T>)

	@Update(onConflict = OnConflictStrategy.REPLACE)
	suspend fun update(entity: T)

	@Delete
	suspend fun delete(entity: T): Int

	@Update
	suspend fun updateAll(vararg entities: T)

	@Delete
	suspend fun deleteAll(vararg entities: T)
}