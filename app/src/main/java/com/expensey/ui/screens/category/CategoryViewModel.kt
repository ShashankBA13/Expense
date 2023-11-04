package com.expensey.ui.screens.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.Category
import com.expensey.data.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application){

	val categoryLiveDataList : LiveData<List<Category>>
	private val categoryRepository : CategoryRepository

	init {
		val expenseyApplication = application as ExpenseyApplication
		val categoryDao =expenseyApplication.database.categoryDao()
		categoryRepository = CategoryRepository(categoryDao)
		categoryLiveDataList = categoryRepository.categoryLiveDataList
	}

	fun addCategory(category : Category) {
		viewModelScope.launch(Dispatchers.IO) {
			categoryRepository.insertCategory(category)
		}
	}

	fun updateCategory(category : Category) {
		viewModelScope.launch {
			categoryRepository.updateCategory(category)
		}
	}

	fun deleteCategory(category : Category) {
		viewModelScope.launch {
			categoryRepository.deleteCategory(category)
		}
	}
}