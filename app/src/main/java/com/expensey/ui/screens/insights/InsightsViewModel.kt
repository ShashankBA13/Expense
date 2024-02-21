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
import com.expensey.data.models.Expense
import com.expensey.data.models.ExpenseSummary
import com.expensey.data.repository.ExpenseRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class InsightsViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "Insights View Model"
    private val expenseRepository: ExpenseRepository
    val mostSpentCategoryCountList: LiveData<List<ExpenseSummary>>
    val expenseFlowList: Flow<List<Expense>>


    init {
        val expenseyApplication = application as ExpenseyApplication
        val expenseDao = expenseyApplication.database.expenseDao()
        expenseRepository = ExpenseRepository(expenseDao, expenseyApplication.baseContext)
        expenseFlowList = expenseRepository.expenseFlowList

        mostSpentCategoryCountList = expenseRepository.getMostSpentCategoryCout()
    }

}