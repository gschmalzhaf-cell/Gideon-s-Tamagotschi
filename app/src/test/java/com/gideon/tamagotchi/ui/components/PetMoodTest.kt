package com.gideon.tamagotchi.ui.components

import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetType
import org.junit.Assert.assertEquals
import org.junit.Test

class PetMoodTest {

    private fun samplePet(
        hunger: Int = 80,
        happiness: Int = 80,
        energy: Int = 80,
        hygiene: Int = 80,
        isAlive: Boolean = true,
        isSleeping: Boolean = false
    ) = Pet(
        id = 1L,
        name = "Gideon",
        type = PetType.BLOB,
        colorHex = 0xFFE0316EL,
        createdAt = 0L,
        hunger = hunger,
        happiness = happiness,
        energy = energy,
        hygiene = hygiene,
        isAlive = isAlive,
        isSleeping = isSleeping
    )

    @Test
    fun `totes Pet hat Stimmung DEAD`() {
        assertEquals(PetMood.DEAD, computePetMood(samplePet(isAlive = false)))
    }

    @Test
    fun `schlafendes Pet hat Stimmung SLEEPING`() {
        assertEquals(PetMood.SLEEPING, computePetMood(samplePet(isSleeping = true, hunger = 10)))
    }

    @Test
    fun `sehr niedrige Gesundheit ergibt SICK`() {
        assertEquals(
            PetMood.SICK,
            computePetMood(samplePet(hunger = 20, happiness = 20, energy = 20, hygiene = 20))
        )
    }

    @Test
    fun `niedriger Hunger ergibt HUNGRY`() {
        assertEquals(PetMood.HUNGRY, computePetMood(samplePet(hunger = 25)))
    }

    @Test
    fun `niedrige Sauberkeit ergibt DIRTY`() {
        assertEquals(PetMood.DIRTY, computePetMood(samplePet(hygiene = 25)))
    }

    @Test
    fun `niedrige Energie ergibt TIRED`() {
        assertEquals(PetMood.TIRED, computePetMood(samplePet(energy = 25)))
    }

    @Test
    fun `hohe Gesundheit ergibt HAPPY`() {
        assertEquals(PetMood.HAPPY, computePetMood(samplePet(hunger = 90, happiness = 85, energy = 80, hygiene = 85)))
    }

    @Test
    fun `mittlere Werte ergeben NEUTRAL`() {
        assertEquals(PetMood.NEUTRAL, computePetMood(samplePet(hunger = 60, happiness = 60, energy = 60, hygiene = 60)))
    }
}
