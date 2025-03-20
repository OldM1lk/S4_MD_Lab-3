package com.example.lab_3.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_3.data.model.Character
import com.example.lab_3.data.remote.RickAndMortyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface RickAndMortyUiState {
    data object Success : RickAndMortyUiState
    data object Error : RickAndMortyUiState
    data object Loading : RickAndMortyUiState
}

class RickAndMortyViewModel : ViewModel() {
    var rickAndMortyUiState: RickAndMortyUiState by mutableStateOf(RickAndMortyUiState.Loading)
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        val pageNumber: Int = (0..42).random()

        viewModelScope.launch(Dispatchers.IO) {
            rickAndMortyUiState = RickAndMortyUiState.Loading
            try {
                Log.d("CharacterViewModel", "Loading characters...")
                val response = RickAndMortyApi.create().getCharacters(pageNumber)
                _characters.value = response.results
                rickAndMortyUiState = RickAndMortyUiState.Success
                Log.d("CharacterViewModel", "Characters loaded: ${response.results.size}")
            } catch (e: IOException) {
                Log.e("CharacterViewModel", "Error loading characters: ", e)
                rickAndMortyUiState = RickAndMortyUiState.Error
            }
        }
    }
}

