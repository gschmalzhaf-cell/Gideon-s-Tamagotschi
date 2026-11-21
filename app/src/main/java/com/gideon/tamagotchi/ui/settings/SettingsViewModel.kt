package com.gideon.tamagotchi.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gideon.tamagotchi.data.Settings
import com.gideon.tamagotchi.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<Settings> = settingsRepository.settings

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setNotificationsEnabled(enabled) }
    }

    fun setSoundsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setSoundsEnabled(enabled) }
    }
}
