package com.example.week5soal2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.week5soal2.ui.theme.Week5Soal2Theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.week5soal2.view.MainAppUI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Soal2()
        }
    }
}

@Composable
fun Soal2() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainAppUI()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal2Preview() {
    Soal2()
}