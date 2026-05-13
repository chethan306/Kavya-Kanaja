package com.example.kavyakanaja.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kavyakanaja.data.models.User
import com.example.kavyakanaja.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _language = MutableStateFlow("kn")
    val language: StateFlow<String> = _language

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    init {
        _isLoggedIn.value = repository.isLoggedIn()
        if (_isLoggedIn.value) {
            _user.value = repository.getUser()
            _favorites.value = repository.getFavorites()
            _language.value = repository.getLanguage()
        }
    }

    fun updateProfile(name: String) {
        repository.updateProfile(name)
        _user.value = repository.getUser()
    }

    fun setLanguage(languageCode: String) {
        repository.setLanguage(languageCode)
        _language.value = languageCode
    }

    fun toggleFavorite(poemId: String) {
        repository.toggleFavorite(poemId)
        _favorites.value = repository.getFavorites()
    }

    fun login(name: String) {
        repository.saveUser(name)
        _user.value = repository.getUser()
        _isLoggedIn.value = true
    }

    fun logout() {
        repository.logout()
        _user.value = null
        _isLoggedIn.value = false
    }
}
