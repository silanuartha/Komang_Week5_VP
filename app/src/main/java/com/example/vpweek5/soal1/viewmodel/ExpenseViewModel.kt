package com.example.vpweek5.soal1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.vpweek5.soal1.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseViewModel : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _totalExpense = MutableStateFlow(0L)
    val totalExpense: StateFlow<Long> = _totalExpense.asStateFlow()

    fun addExpense(name: String, amount: Long): Boolean {
        if (amount <= 0) return false

        val newExpense = Expense(name = name, amount = amount)
        _expenses.value = _expenses.value + newExpense
        calculateTotal()
        return true
    }

    fun deleteExpense(id: String) {
        _expenses.value = _expenses.value.filter { it.id != id }
        calculateTotal()
    }

    fun editExpense(id: String, newName: String, newAmount: Long): Boolean {
        if (newAmount <= 0) return false
        _expenses.value = _expenses.value.map {
            if (it.id == id) it.copy(name = newName, amount = newAmount) else it
        }
        calculateTotal()
        return true
    }

    private fun calculateTotal() {
        _totalExpense.value = _expenses.value.sumOf { it.amount }
    }
}