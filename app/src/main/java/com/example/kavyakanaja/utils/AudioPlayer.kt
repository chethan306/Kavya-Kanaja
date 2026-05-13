package com.example.kavyakanaja.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun play(url: String) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, Uri.parse(url))
            prepareAsync()
            setOnPreparedListener { start() }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
