package com.example.vpweek5.soal1.model

import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var amount: Long
)