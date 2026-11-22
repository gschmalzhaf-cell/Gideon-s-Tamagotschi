package com.gideon.tamagotchi.ui.create

import androidx.lifecycle.ViewModel
import com.gideon.tamagotchi.data.PetRepository
import com.gideon.tamagotchi.domain.model.PetType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    suspend fun createPet(name: String, type: PetType, colorHex: Long) {
        petRepository.createPet(name.trim(), type, colorHex)
    }
}
