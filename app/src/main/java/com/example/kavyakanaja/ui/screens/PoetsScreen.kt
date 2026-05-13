package com.example.kavyakanaja.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kavyakanaja.viewmodel.PoetViewModel
import com.example.kavyakanaja.data.models.Poet

@Composable
fun PoetsScreen(viewModel: PoetViewModel, onPoetClick: (String) -> Unit) {
    val poets by viewModel.poets.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(text = "Poets Corner", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(poets) { poet ->
            PoetListItem(poet = poet, onClick = { onPoetClick(poet.id) })
        }
    }
}

@Composable
fun PoetListItem(poet: Poet, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = poet.name, style = MaterialTheme.typography.titleMedium)
            Text(text = poet.famousWorks.joinToString(", "), style = MaterialTheme.typography.bodySmall, maxLines = 1)
        }
    }
}
