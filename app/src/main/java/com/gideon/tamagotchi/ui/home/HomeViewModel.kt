package com.gideon.tamagotchi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gideon.tamagotchi.data.PetRepository
import com.gideon.tamagotchi.domain.model.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val pet: Pet? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    petRepository: PetRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = petRepository.observePet()
        .map { HomeUiState(isLoading = false, pet = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}
