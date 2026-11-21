package com.gideon.tamagotchi.domain

import com.gideon.tamagotchi.domain.model.PetStage
import org.junit.Assert.assertEquals
import org.junit.Test

class EvolutionEngineTest {

    private val now = 10L * 24 * 60 * 60 * 1000
    private val hour = 60L * 60 * 1000
    private val day = 24 * hour

    @Test
    fun `Baby in den ersten 24 Stunden`() {
        assertEquals(PetStage.BABY, EvolutionEngine.stageFor(now - 12 * hour, health = 100, now = now))
    }

    @Test
    fun `Kind nach 1 bis 3 Tagen`() {
        assertEquals(PetStage.CHILD, EvolutionEngine.stageFor(now - 2 * day, health = 100, now = now))
    }

    @Test
    fun `Teenager nach 3 bis 7 Tagen`() {
        assertEquals(PetStage.TEEN, EvolutionEngine.stageFor(now - 5 * day, health = 100, now = now))
    }

    @Test
    fun `Erwachsener ab 7 Tagen`() {
        assertEquals(PetStage.ADULT, EvolutionEngine.stageFor(now - 10 * day, health = 100, now = now))
    }

    @Test
    fun `bei niedriger Gesundheit eine Stufe zurueck`() {
        assertEquals(PetStage.TEEN, EvolutionEngine.stageFor(now - 10 * day, health = 10, now = now))
    }

    @Test
    fun `Baby bleibt auch mit niedriger Gesundheit Baby`() {
        assertEquals(PetStage.BABY, EvolutionEngine.stageFor(now - 12 * hour, health = 10, now = now))
    }
}
