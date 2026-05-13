package com.example.kavyakanaja.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.data.models.Poem
import com.example.kavyakanaja.viewmodel.AudioViewModel
import com.example.kavyakanaja.viewmodel.UserViewModel
import com.example.kavyakanaja.ui.components.SectionHeader

import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemDetailScreen(
    poem: Poem?,
    audioViewModel: AudioViewModel,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    if (poem == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Poem not found", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    val context = LocalContext.current
    val isPlaying by audioViewModel.isPlaying.collectAsState()
    val currentUrl by audioViewModel.currentUrl.collectAsState()
    val favorites by userViewModel.favorites.collectAsState()
    val isFavorite = favorites.contains(poem.id)
    var selectedWordMeaning by remember { mutableStateOf<String?>(null) }
    
    fun sharePoem() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${poem.title}\n\n${poem.poemText}\n\n- ${poem.poetName}\n\nShared via Kavya Kanaja")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun downloadPoem() {
        // In a real app, this would save to a file or database
        Toast.makeText(context, "Poem downloaded successfully to offline storage", Toast.LENGTH_SHORT).show()
    }

    if (selectedWordMeaning != null) {
        AlertDialog(
            onDismissRequest = { selectedWordMeaning = null },
            confirmButton = {
                TextButton(onClick = { selectedWordMeaning = null }) {
                    Text("OK")
                }
            },
            title = { Text("Word Meaning", style = MaterialTheme.typography.titleLarge) },
            text = { Text(selectedWordMeaning!!, style = MaterialTheme.typography.bodyLarge) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { downloadPoem() }) {
                        Icon(Icons.Default.Download, contentDescription = "Download")
                    }
                    IconButton(onClick = { sharePoem() }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { userViewModel.toggleFavorite(poem.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save",
                            tint = if (isFavorite) Color.Red else LocalContentColor.current
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    ),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = poem.title,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "by ${poem.poetName}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Audio Player Controls
            poem.audioUrl?.let { url ->
                Surface(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MusicNote, contentDescription = null)
                            Spacer(Modifier.width(12.dp))
                            Text("Listen to this poem", style = MaterialTheme.typography.titleMedium)
                        }
                        IconButton(onClick = { audioViewModel.togglePlay(url) }) {
                            Icon(
                                imageVector = if (isPlaying && currentUrl == url) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Poem Content
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    val annotatedPoemText = buildAnnotatedString {
                        val words = poem.poemText.split(Regex("(?<=\\s)|(?=\\s)"))
                        words.forEach { word ->
                            val trimmedWord = word.trim()
                            if (poem.wordMeanings.containsKey(trimmedWord)) {
                                pushStringAnnotation(tag = "WORD", annotation = poem.wordMeanings[trimmedWord]!!)
                                withStyle(style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                                )) {
                                    append(word)
                                }
                                pop()
                            } else {
                                append(word)
                            }
                        }
                    }

                    ClickableText(
                        text = annotatedPoemText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 20.sp,
                            lineHeight = 32.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = { offset ->
                            annotatedPoemText.getStringAnnotations(tag = "WORD", start = offset, end = offset)
                                .firstOrNull()?.let { annotation ->
                                    selectedWordMeaning = annotation.item
                                }
                        }
                    )
                }
            }

            // Bhavartha Section
            SectionHeader(title = "Bhavartha (Explanation)")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Text(
                    text = poem.bhavartha,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
