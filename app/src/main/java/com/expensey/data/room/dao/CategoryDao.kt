package com.expensey.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.expensey.data.models.Category
import kotlinx.coroutines.flow.Flow

/**
 * [ROOM] DAO for [Category] related operations
 */
@Dao
abstract class CategoryDao : BaseDao<Category> {

	@Query("SELECT * FROM category")
	abstract fun fetchAllCategories() : LiveData<List<Category>>

	@Query("SELECT * FROM category WHERE name = :name")
	abstract suspend fun getCategoryWithName(name : String): Category?

	@Query("SELECT * FROM category WHERE id = :categoryId")
	abstract fun getCategoryById(categoryId : Int) : Flow<Category>
}