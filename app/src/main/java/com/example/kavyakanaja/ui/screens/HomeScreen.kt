package com.example.kavyakanaja.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.viewmodel.PoemViewModel
import com.example.kavyakanaja.data.models.Poem
import com.example.kavyakanaja.ui.components.SectionHeader

import com.example.kavyakanaja.data.models.ContentType

@Composable
fun HomeScreen(viewModel: PoemViewModel, onPoemClick: (String) -> Unit) {
    val poems by viewModel.poems.collectAsState()
    val poemOfTheDay by viewModel.poemOfTheDay.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ContentType.POEM) }
    
    val categories = listOf("Patriotic", "Nature", "Devotional", "Social", "Love", "Folk", "Epic", "Modern", "Classical", "Vachana", "Dasa Sahitya")

    val filteredPoems = poems.filter { 
        val matchesType = it.type == selectedType
        val matchesSearch = it.title.contains(searchQuery, ignoreCase = true) || it.poetName.contains(searchQuery, ignoreCase = true)
        matchesType && matchesSearch
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        SearchBar(searchQuery) { searchQuery = it }
        
        if (searchQuery.isEmpty()) {
            ScrollableTabRow(
                selectedTabIndex = selectedType.ordinal,
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                ContentType.values().forEach { type ->
                    Tab(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (searchQuery.isEmpty()) {
                item {
                    CategorySection(categories)
                }

                if (selectedType == ContentType.POEM) {
                    poemOfTheDay?.let { poem ->
                        item {
                            PoemOfTheDaySection(poem = poem, onClick = { onPoemClick(poem.id) })
                        }
                    }
                }
            }

            item {
                SectionHeader(title = if (searchQuery.isEmpty()) "Explore ${selectedType.name.lowercase().replaceFirstChar { it.uppercase() }}s" else "Search Results")
            }

            items(filteredPoems) { poem ->
                PoemCard(poem = poem, onClick = { onPoemClick(poem.id) })
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Discover the soul of Kannada poetry",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text("Search poems or poets...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        )
    )
}

@Composable
fun CategorySection(categories: List<String>) {
    val categoryColors = listOf(
        Color(0xFFFFEBEE), // Light Red
        Color(0xFFE3F2FD), // Light Blue
        Color(0xFFF1F8E9), // Light Green
        Color(0xFFFFF3E0), // Light Orange
        Color(0xFFF3E5F5)  // Light Purple
    )

    Column {
        SectionHeader(title = "Categories")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.zip(categoryColors)) { (category, color) ->
                FilterChip(
                    selected = false,
                    onClick = { /* TODO */ },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = color,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = null
                )
            }
        }
    }
}

@Composable
fun PoemOfTheDaySection(poem: Poem, onClick: () -> Unit) {
    Column {
        SectionHeader(title = "Poem of the Day")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(220.dp),
            shape = RoundedCornerShape(24.dp),
            onClick = onClick
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "FEATURED",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = poem.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "by ${poem.poetName}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PoemCard(poem: Poem, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = poem.title.first().toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = poem.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = poem.poetName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
