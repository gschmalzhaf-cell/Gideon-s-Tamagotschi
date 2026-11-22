package com.gideon.tamagotchi.ui.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gideon.tamagotchi.data.PetRepository
import com.gideon.tamagotchi.domain.model.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GameOverViewModel @Inject constructor(
    petRepository: PetRepository
) : ViewModel() {

    val pet: StateFlow<Pet?> = petRepository.observePet()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}
