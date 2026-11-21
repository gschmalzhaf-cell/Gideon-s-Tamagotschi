package com.gideon.tamagotchi.data

import com.gideon.tamagotchi.data.local.PetDao
import com.gideon.tamagotchi.data.local.PetEntity
import com.gideon.tamagotchi.domain.TickCalculator
import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetAction
import com.gideon.tamagotchi.domain.model.PetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetRepository @Inject constructor(
    private val petDao: PetDao
) {

    fun observePet(): Flow<Pet?> = petDao.observePet().map { it?.toDomain() }

    suspend fun createPet(name: String, type: PetType, colorHex: Long) {
        val now = System.currentTimeMillis()
        petDao.deleteAll()
        petDao.insert(
            PetEntity(
                name = name,
                type = type.name,
                colorHex = colorHex,
                createdAt = now,
                hunger = 80,
                happiness = 80,
                energy = 100,
                hygiene = 80,
                isAlive = true,
                isSleeping = false,
                lastTickAt = now
            )
        )
    }

    suspend fun applyAction(action: PetAction) {
        val current = petDao.getPet() ?: return
        petDao.update(TickCalculator.applyAction(current.toDomain(), action).toEntity())
    }

    suspend fun applyElapsedTicks() {
        syncOffline()
    }

    suspend fun syncOffline(): OfflineReport? {
        val current = petDao.getPet() ?: return null
        val now = System.currentTimeMillis()
        val elapsedMinutes = (now - current.lastTickAt) / 60_000L
        if (elapsedMinutes <= 0) return null
        val wasAlive = current.isAlive
        val updated = TickCalculator.decay(current.toDomain(), elapsedMinutes)
            .copy(lastTickAt = now)
        petDao.update(updated.toEntity())
        return OfflineReport(
            minutes = elapsedMinutes,
            diedDuringAbsence = wasAlive && !updated.isAlive
        )
    }
}
