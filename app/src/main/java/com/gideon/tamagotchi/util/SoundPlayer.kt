package com.gideon.tamagotchi.util

import android.content.Context
import android.media.RingtoneManager
import com.gideon.tamagotchi.data.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository
) {

    fun playActionSound() {
        if (!settingsRepository.settings.value.soundsEnabled) return
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(context, uri)?.play()
    }
}
