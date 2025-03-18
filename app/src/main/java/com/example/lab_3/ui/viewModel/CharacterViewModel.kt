package com.example.lab_3.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_3.data.model.Character
import com.example.lab_3.data.remote.RickAndMortyApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    fun fetchCharacters() {
        val pageNumber: Int = (0..42).random()
        viewModelScope.launch {
            try {
                Log.d("CharacterViewModel", "Loading characters...")
                val response = RickAndMortyApi.retrofitService.getCharacters(pageNumber)
                _characters.value = response.results
                Log.d("CharacterViewModel", "Characters loaded: ${response.results.size}")
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error loading characters: ", e)
            }
        }
    }

    init {
        fetchCharacters()
    }
}