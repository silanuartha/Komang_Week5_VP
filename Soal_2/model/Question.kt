package com.example.week5soal2.model

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

data class WrongAnswer(
    val question: String,
    val userAnswer: String,
    val correctAnswer: String
)

enum class AppScreen { START, QUIZ, RESULT }