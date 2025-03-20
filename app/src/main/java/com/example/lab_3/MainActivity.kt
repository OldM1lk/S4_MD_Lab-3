package com.example.lab_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab_3.ui.RickAndMortyApp
import com.example.lab_3.ui.theme.Lab_3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab_3Theme(darkTheme = true) {
                RickAndMortyApp()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RickAndMortyAppPreview() {
    Lab_3Theme(darkTheme = true) {
        RickAndMortyApp()
    }
}