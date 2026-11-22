package com.gideon.tamagotchi.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

data class Settings(
    val notificationsEnabled: Boolean = true,
    val soundsEnabled: Boolean = true
)

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    appScope: CoroutineScope
) {

    val settings: StateFlow<Settings> = dataStore.data
        .map { prefs ->
            Settings(
                notificationsEnabled = prefs[Keys.notificationsEnabled] ?: true,
                soundsEnabled = prefs[Keys.soundsEnabled] ?: true
            )
        }
        .stateIn(appScope, SharingStarted.Eagerly, Settings())

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.notificationsEnabled] = enabled }
    }

    suspend fun setSoundsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.soundsEnabled] = enabled }
    }

    private object Keys {
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val soundsEnabled = booleanPreferencesKey("sounds_enabled")
    }
}
