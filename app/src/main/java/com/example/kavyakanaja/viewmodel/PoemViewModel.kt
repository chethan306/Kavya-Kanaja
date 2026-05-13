package com.example.kavyakanaja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kavyakanaja.data.models.Poem
import com.example.kavyakanaja.repository.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PoemViewModel(private val repository: PoemRepository) : ViewModel() {

    private val _poems = MutableStateFlow<List<Poem>>(emptyList())
    val poems: StateFlow<List<Poem>> = _poems

    private val _poemOfTheDay = MutableStateFlow<Poem?>(null)
    val poemOfTheDay: StateFlow<Poem?> = _poemOfTheDay

    fun loadPoems(language: String) {
        viewModelScope.launch {
            _poems.value = repository.getAllPoems(language)
            _poemOfTheDay.value = repository.getPoemOfTheDay(language)
        }
    }
}
