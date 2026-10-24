package com.example.vpweek5.soal1.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.vpweek5.soal1.model.Expense
import com.example.vpweek5.soal1.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val expenses by viewModel.expenses.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()

    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Expenses", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Total Expense : Rp.$totalExpense", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Expense Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = expenseAmount,
                onValueChange = {
                    expenseAmount = it
                    isError = false
                },
                label = { Text("Amount of Expense") },
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (isError) Text("Amount must be greater than 0", color = Color.Red)
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    val amount = expenseAmount.toLongOrNull() ?: 0L
                    if (amount <= 0) {
                        isError = true
                    } else {
                        viewModel.addExpense(expenseName, amount)
                        expenseName = ""
                        expenseAmount = ""
                        isError = false
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(expenses) { expense ->
                ExpenseItem(
                    expense = expense,
                    onDelete = { viewModel.deleteExpense(it.id) },
                    onEditAmount = { id, newAmount ->
                        viewModel.editExpense(id, expense.name, newAmount)
                    }
                )
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onDelete: (Expense) -> Unit, onEditAmount: (String, Long) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var editAmountText by remember { mutableStateOf(expense.amount.toString()) }
    var isEditError by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Name : ${expense.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = editAmountText,
                        onValueChange = {
                            editAmountText = it
                            isEditError = false
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = isEditError,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                    if (isEditError) {
                        Text(text = "Amount > 0", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row {
                    IconButton(onClick = {
                        val amount = editAmountText.toLongOrNull() ?: 0L
                        if (amount <= 0) {
                            isEditError = true
                        } else {
                            onEditAmount(expense.id, amount)
                            isEditing = false
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = Color(0xFF4CAF50))
                    }
                    IconButton(onClick = {
                        isEditing = false
                        editAmountText = expense.amount.toString()
                        isEditError = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.Gray)
                    }
                }
            } else {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Name : ${expense.name}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Rp ${expense.amount}", style = MaterialTheme.typography.bodyMedium)
                }
                Row {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFFFBC02D))
                    }
                    IconButton(onClick = { onDelete(expense) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}