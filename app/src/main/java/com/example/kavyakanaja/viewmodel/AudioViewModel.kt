package com.example.kavyakanaja.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kavyakanaja.utils.AudioPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AudioViewModel(private val audioPlayer: AudioPlayer) : ViewModel() {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentUrl = MutableStateFlow<String?>(null)
    val currentUrl: StateFlow<String?> = _currentUrl

    fun togglePlay(url: String) {
        if (_currentUrl.value == url) {
            if (_isPlaying.value) {
                audioPlayer.pause()
                _isPlaying.value = false
            } else {
                audioPlayer.resume()
                _isPlaying.value = true
            }
        } else {
            audioPlayer.play(url)
            _currentUrl.value = url
            _isPlaying.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }
}
