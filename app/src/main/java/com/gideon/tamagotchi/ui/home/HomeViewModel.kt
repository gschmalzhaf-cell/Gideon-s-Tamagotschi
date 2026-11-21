package com.gideon.tamagotchi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gideon.tamagotchi.data.OfflineReport
import com.gideon.tamagotchi.data.PetRepository
import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val pet: Pet? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = petRepository.observePet()
        .map { HomeUiState(isLoading = false, pet = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    private val _offlineReport = MutableSharedFlow<OfflineReport>(extraBufferCapacity = 1)
    val offlineReport: SharedFlow<OfflineReport> = _offlineReport.asSharedFlow()

    init {
        viewModelScope.launch {
            val report = petRepository.syncOffline()
            if (report != null && report.minutes >= OFFLINE_REPORT_MIN_MINUTES) {
                _offlineReport.emit(report)
            }
        }
    }

    fun onAction(action: PetAction) {
        viewModelScope.launch {
            petRepository.applyAction(action)
        }
    }

    private companion object {
        const val OFFLINE_REPORT_MIN_MINUTES = 5L
    }
}
