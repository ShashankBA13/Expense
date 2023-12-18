package com.expensey.ui.screens.insights

import android.app.Application
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.expensey.ExpenseyApplication
import com.expensey.data.models.CategoryCount
import com.expensey.data.models.ExpenseSummary
import com.expensey.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class InsightsViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseRepository: ExpenseRepository
    private val _mostSpentCategoryCountList = MutableLiveData<List<ExpenseSummary>>()
    val mostSpentCategoryCountList: LiveData<List<ExpenseSummary>>

    init {
        val expenseyApplication = application as ExpenseyApplication
        val expenseDao = expenseyApplication.database.expenseDao()
        expenseRepository = ExpenseRepository(expenseDao, expenseyApplication.baseContext)

        mostSpentCategoryCountList = expenseRepository.getMostSpentCategoryCout()
    }
}
