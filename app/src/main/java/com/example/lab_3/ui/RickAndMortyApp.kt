package com.example.lab_3.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab_3.ui.screens.HomeScreen
import com.example.lab_3.ui.screens.RickAndMortyViewModel

@Composable
@SuppressLint("StateFlowValueCalledInComposition")
fun RickAndMortyApp(viewModel: RickAndMortyViewModel = viewModel()) {
    Scaffold(
        topBar = { AppTopBar(viewModel) }
    ) {
        HomeScreen(
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppTopBar(viewModel: RickAndMortyViewModel) {
    TopAppBar(
        title = { Text(text = "Rick and Morty") },
        actions = {
            IconButton(
                onClick = {
                    viewModel.fetchCharacters()
                }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Обновить")
            }
        }
    )
}