package com.example.kavyakanaja.repository

import android.content.Context
import com.example.kavyakanaja.data.models.User
import com.example.kavyakanaja.data.models.UserPreferences

class UserRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("is_logged_in", loggedIn).apply()
    }

    fun getUser(): User? {
        val name = prefs.getString("user_name", null) ?: return null
        return User(id = "1", name = name)
    }

    fun saveUser(name: String) {
        prefs.edit().putString("user_name", name).apply()
        setLoggedIn(true)
    }

    fun updateProfile(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    fun setLanguage(languageCode: String) {
        prefs.edit().putString("language", languageCode).apply()
    }

    fun getLanguage(): String {
        return prefs.getString("language", "kn") ?: "kn"
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun getFavorites(): Set<String> {
        return prefs.getStringSet("favorites", emptySet()) ?: emptySet()
    }

    fun toggleFavorite(poemId: String) {
        val favorites = getFavorites().toMutableSet()
        if (favorites.contains(poemId)) {
            favorites.remove(poemId)
        } else {
            favorites.add(poemId)
        }
        prefs.edit().putStringSet("favorites", favorites).apply()
    }
}
