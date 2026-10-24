package com.example.week5soal2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.week5soal2.model.AppScreen
import com.example.week5soal2.model.Question
import com.example.week5soal2.model.WrongAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {
    private val allQuestions = listOf(
        Question("Apa ibu kota dari Jawa Timur?", listOf("Malang", "Surabaya", "Kediri", "Madiun"), "Surabaya"),
        Question("Bahasa pemrograman yang digunakan untuk Android Studio adalah?", listOf("Python", "Kotlin", "PHP", "Swift"), "Kotlin"),
        Question("Ada berapa bulan dalam satu tahun kalender?", listOf("10", "11", "12", "14"), "12"),
        Question("Natal dirayakan pada bulan?", listOf("Oktober", "November", "Desember", "Januari"), "Desember"),
        Question("Hari Kemerdekaan Indonesia diperingati pada tanggal?", listOf("1 Juni", "17 Agustus", "28 Oktober", "10 November"), "17 Agustus")
    )

    private val _currentScreen = MutableStateFlow(AppScreen.START)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _activeQuestions = MutableStateFlow<List<Question>>(emptyList())
    val activeQuestions: StateFlow<List<Question>> = _activeQuestions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _wrongAnswers = MutableStateFlow<List<WrongAnswer>>(emptyList())
    val wrongAnswers: StateFlow<List<WrongAnswer>> = _wrongAnswers.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    fun startQuiz(questionCount: Int): Boolean {
        if (questionCount !in 1..allQuestions.size) return false
        _activeQuestions.value = allQuestions.shuffled().take(questionCount)
        _currentIndex.value = 0
        _score.value = 0
        _wrongAnswers.value = emptyList()
        _currentScreen.value = AppScreen.QUIZ
        return true
    }

    fun submitAnswer(userAnswer: String) {
        val currentQ = _activeQuestions.value[_currentIndex.value]
        if (userAnswer == currentQ.correctAnswer) {
            _score.value += 1
        } else {
            _wrongAnswers.value = _wrongAnswers.value + WrongAnswer(
                question = currentQ.text,
                userAnswer = userAnswer,
                correctAnswer = currentQ.correctAnswer
            )
        }

        if (_currentIndex.value < _activeQuestions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            _currentScreen.value = AppScreen.RESULT
        }
    }

    fun restartQuiz() {
        _currentScreen.value = AppScreen.START
    }

    fun getMaxQuestions(): Int = allQuestions.size
}