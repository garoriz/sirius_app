package com.example.siriusapp.features.task.presentation.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class SoundService : Service() {

    private var mediaPlayer: MediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    inner class LocaleBinder : Binder() {

        fun play(sound: Int) {
            playSound(sound)
        }
    }

    override fun onBind(p0: Intent?): IBinder = LocaleBinder()

    private fun playSound(song: Int) {
        mediaPlayer = MediaPlayer.create(applicationContext, song)
        mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(1.4F);
        mediaPlayer.run {
            start()
            setOnCompletionListener {
                stop()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
