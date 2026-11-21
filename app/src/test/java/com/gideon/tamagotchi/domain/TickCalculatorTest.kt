package com.gideon.tamagotchi.domain

import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetAction
import com.gideon.tamagotchi.domain.model.PetType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TickCalculatorTest {

    private fun pet(
        hunger: Int = 80,
        happiness: Int = 80,
        energy: Int = 80,
        hygiene: Int = 80,
        isAlive: Boolean = true,
        isSleeping: Boolean = false
    ) = Pet(
        id = 1,
        name = "Test",
        type = PetType.BLOB,
        colorHex = 0xFFD6336C,
        createdAt = 0L,
        hunger = hunger,
        happiness = happiness,
        energy = energy,
        hygiene = hygiene,
        isAlive = isAlive,
        isSleeping = isSleeping,
        lastTickAt = 0L
    )

    @Test
    fun `decay senkt alle Stats pro Minute`() {
        val result = TickCalculator.decay(pet(), minutes = 10)
        assertEquals(60, result.hunger)
        assertEquals(70, result.hygiene)
        assertEquals(70, result.happiness)
        assertEquals(70, result.energy)
    }

    @Test
    fun `decay clampt Stats auf 0`() {
        val result = TickCalculator.decay(pet(hunger = 5), minutes = 10)
        assertEquals(0, result.hunger)
        assertTrue(result.isAlive)
    }

    @Test
    fun `decay ohne Minuten aendert nichts`() {
        val p = pet()
        assertEquals(p, TickCalculator.decay(p, minutes = 0))
    }

    @Test
    fun `schlafen regeneriert Energie und weckt bei 100 automatisch auf`() {
        val result = TickCalculator.decay(pet(energy = 95, isSleeping = true), minutes = 1)
        assertEquals(100, result.energy)
        assertFalse(result.isSleeping)
    }

    @Test
    fun `schlafen verbraucht Hunger schneller`() {
        val result = TickCalculator.decay(pet(isSleeping = true), minutes = 10)
        assertEquals(40, result.hunger)
    }

    @Test
    fun `Tod bei Gesundheit 0`() {
        val result = TickCalculator.decay(
            pet(hunger = 0, happiness = 0, energy = 0, hygiene = 0),
            minutes = 1
        )
        assertFalse(result.isAlive)
        assertFalse(result.isSleeping)
    }

    @Test
    fun `totes Pet reagiert weder auf Ticks noch auf Aktionen`() {
        val dead = pet(isAlive = false)
        assertEquals(dead, TickCalculator.decay(dead, minutes = 100))
        assertEquals(dead, TickCalculator.applyAction(dead, PetAction.FEED))
    }

    @Test
    fun `fuettern erhoeht Hunger um 25`() {
        assertEquals(75, TickCalculator.applyAction(pet(hunger = 50), PetAction.FEED).hunger)
    }

    @Test
    fun `Aktionen clamppen auf 100`() {
        assertEquals(100, TickCalculator.applyAction(pet(hunger = 90), PetAction.FEED).hunger)
        assertEquals(100, TickCalculator.applyAction(pet(hygiene = 95), PetAction.WASH).hygiene)
    }

    @Test
    fun `spielen gibt Glueck und kostet Energie`() {
        val result = TickCalculator.applyAction(pet(happiness = 50, energy = 50), PetAction.PLAY)
        assertEquals(70, result.happiness)
        assertEquals(40, result.energy)
    }

    @Test
    fun `schlafen toggelt den Schlafzustand`() {
        val awake = pet(isSleeping = false)
        assertTrue(TickCalculator.applyAction(awake, PetAction.SLEEP).isSleeping)
        val sleeping = pet(isSleeping = true)
        assertFalse(TickCalculator.applyAction(sleeping, PetAction.SLEEP).isSleeping)
    }

    @Test
    fun `medizin heilt den niedrigsten Stat um 25`() {
        val result = TickCalculator.applyAction(pet(hunger = 10), PetAction.MEDICINE)
        assertEquals(35, result.hunger)
    }
}
