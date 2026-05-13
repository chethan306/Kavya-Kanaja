package com.example.kavyakanaja.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kavyakanaja.viewmodel.UserViewModel

data class Language(val name: String, val code: String)

val supportedLanguages = listOf(
    Language("Kannada", "kn"),
    Language("English", "en"),
    Language("Hindi", "hi"),
    Language("Tamil", "ta"),
    Language("Telugu", "te"),
    Language("Malayalam", "ml"),
    Language("Urdu", "ur"),
    Language("Marathi", "mr"),
    Language("Bengali", "bn"),
    Language("Gujarati", "gu"),
    Language("Punjabi", "pa")
)

@Composable
fun SettingsScreen(userViewModel: UserViewModel) {
    val currentLanguageCode by userViewModel.language.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    val currentLanguage = supportedLanguages.find { it.code == currentLanguageCode }?.name ?: "Kannada"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showLanguageDialog = true }
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Language", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Selected: $currentLanguage", style = MaterialTheme.typography.bodyMedium)
                }
                TextButton(onClick = { showLanguageDialog = true }) {
                    Text("Change")
                }
            }
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Select Language") },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(supportedLanguages) { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    userViewModel.setLanguage(lang.code)
                                    showLanguageDialog = false
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = lang.name)
                            if (lang.code == currentLanguageCode) {
                                RadioButton(selected = true, onClick = null)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
