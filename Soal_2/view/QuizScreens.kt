package com.example.week5soal2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.week5soal2.model.AppScreen
import com.example.week5soal2.viewmodel.QuizViewModel

@Composable
fun MainAppUI(viewModel: QuizViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    when (currentScreen) {
        AppScreen.START -> StartScreen(viewModel)
        AppScreen.QUIZ -> QuizScreen(viewModel)
        AppScreen.RESULT -> ResultScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(viewModel: QuizViewModel) {
    var inputCount by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val maxQ = viewModel.getMaxQuestions()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mini Quiz", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("How many questions do you want? (1 - $maxQ)")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputCount,
            onValueChange = {
                inputCount = it
                isError = false
            },
            label = { Text("Number of questions") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = isError,
            supportingText = {
                if (isError) Text("Maximum is $maxQ questions", color = Color.Red)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val count = inputCount.toIntOrNull() ?: 0
                if (!viewModel.startQuiz(count)) {
                    isError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }
    }
}

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val questions by viewModel.activeQuestions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val currentQuestion = questions[currentIndex]

    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Question ${currentIndex + 1}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(currentQuestion.text, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        currentQuestion.options.forEach { option ->
            Button(
                onClick = { selectedOption = option },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == option) Color(0xFF3F51B5) else Color(0xFFE8EAF6),
                    contentColor = if (selectedOption == option) Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(option)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (selectedOption.isNotEmpty()) {
                    viewModel.submitAnswer(selectedOption)
                    selectedOption = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedOption.isNotEmpty()
        ) {
            Text("Next")
        }
    }
}

@Composable
fun ResultScreen(viewModel: QuizViewModel) {
    val score by viewModel.score.collectAsState()
    val questions by viewModel.activeQuestions.collectAsState()
    val wrongAnswers by viewModel.wrongAnswers.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Quiz Finished!", style = MaterialTheme.typography.headlineMedium)
        Text("Your Score: $score / ${questions.size}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (wrongAnswers.isEmpty()) {
            Text("Perfect! No wrong answers.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(wrongAnswers) { wrong ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(wrong.question, fontWeight = FontWeight.Bold)
                        Text("Your Answer: ${wrong.userAnswer}", color = Color.Red)
                        Text("Correct Answer: ${wrong.correctAnswer}", color = Color.Gray)
                        Divider(modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f, fill = false))

        Button(
            onClick = { viewModel.restartQuiz() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text("Restart Quiz")
        }
    }
}