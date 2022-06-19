package com.xmh.andlenter

import android.annotation.SuppressLint
import android.media.AudioManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.core.content.getSystemService

@SuppressLint("NewApi")
class AndlenterTileService : TileService() {

    override fun onClick() {
        val tile = qsTile ?: return
        val audioManager = getSystemService<AudioManager>() ?: return

        when (tile.state) {
            Tile.STATE_ACTIVE -> {
                tile.state = Tile.STATE_INACTIVE
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                audioManager.setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    0,
                    AudioManager.FLAG_SHOW_UI
                )
            }
            Tile.STATE_INACTIVE -> {
                tile.state = Tile.STATE_ACTIVE
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                audioManager.setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                    AudioManager.FLAG_SHOW_UI
                )
            }
        }

        tile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onStopListening() {
        super.onStopListening()
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        val audioManager = getSystemService<AudioManager>() ?: return
        tile.state = when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> Tile.STATE_ACTIVE
            AudioManager.RINGER_MODE_SILENT, AudioManager.RINGER_MODE_VIBRATE -> Tile.STATE_INACTIVE
            else -> Tile.STATE_INACTIVE
        }
        tile.updateTile()
    }
}
