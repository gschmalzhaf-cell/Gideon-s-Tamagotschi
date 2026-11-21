package com.gideon.tamagotchi.data

import com.gideon.tamagotchi.data.local.PetDao
import com.gideon.tamagotchi.data.local.PetEntity
import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetRepository @Inject constructor(
    private val petDao: PetDao
) {

    fun observePet(): Flow<Pet?> = petDao.observePet().map { it?.toDomain() }

    suspend fun createPet(name: String, type: PetType, colorHex: Long) {
        petDao.insert(
            PetEntity(
                name = name,
                type = type.name,
                colorHex = colorHex,
                createdAt = System.currentTimeMillis(),
                hunger = 80,
                happiness = 80,
                energy = 100,
                hygiene = 80,
                isAlive = true
            )
        )
    }
}
