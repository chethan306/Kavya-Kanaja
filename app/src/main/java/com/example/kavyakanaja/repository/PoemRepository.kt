package com.example.kavyakanaja.repository

import android.content.Context
import com.example.kavyakanaja.data.models.Poem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class PoemRepository(private val context: Context) {
    private val gson = Gson()

    fun getAllPoems(language: String = "kn"): List<Poem> {
        val fileName = when (language) {
            "kn" -> "poems_kn.json"
            "hi" -> "poems_hi.json"
            "ta" -> "poems_ta.json"
            "te" -> "poems_te.json"
            "ml" -> "poems_ml.json"
            "en" -> "poems_en.json"
            else -> "poems_kn.json" // Fallback to Kannada
        }

        return try {
            val inputStream = context.assets.open(fileName)
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Poem>>() {}.type
            gson.fromJson(reader, type)
        } catch (e: Exception) {
            // If specific language file doesn't exist, try Kannada as default
            if (fileName != "poems_kn.json") {
                loadDefaultPoems()
            } else {
                emptyList()
            }
        }
    }

    private fun loadDefaultPoems(): List<Poem> {
        return try {
            val inputStream = context.assets.open("poems_kn.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Poem>>() {}.type
            gson.fromJson(reader, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getPoemById(id: String, language: String): Poem? {
        return getAllPoems(language).find { it.id == id }
    }

    fun getPoemOfTheDay(language: String): Poem? {
        val poems = getAllPoems(language)
        if (poems.isEmpty()) return null
        
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return poems[dayOfYear % poems.size]
    }
}
