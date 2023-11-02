package com.expensey.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.expensey.data.models.Category

/**
 * [ROOM] DAO for [Category] related operations
 */
@Dao
abstract class CategoryDao : BaseDao<Category> {

	@Query("SELECT * FROM category WHERE name = :name")
	abstract suspend fun getCategoryWithName(name : String): Category?
}