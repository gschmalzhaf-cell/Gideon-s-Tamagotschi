package com.gideon.tamagotchi.domain

import com.gideon.tamagotchi.R
import org.junit.Assert.assertEquals
import org.junit.Test

class BondEngineTest {

    private val minute = 60_000L

    @Test
    fun `Level steigt alle 100 Bond-Punkte`() {
        assertEquals(1, BondEngine.level(0))
        assertEquals(1, BondEngine.level(99))
        assertEquals(2, BondEngine.level(100))
        assertEquals(3, BondEngine.level(250))
    }

    @Test
    fun `Titel pro Level`() {
        assertEquals(R.string.bond_level_1, BondEngine.titleResFor(1))
        assertEquals(R.string.bond_level_2, BondEngine.titleResFor(2))
        assertEquals(R.string.bond_level_3, BondEngine.titleResFor(3))
        assertEquals(R.string.bond_level_4, BondEngine.titleResFor(4))
        assertEquals(R.string.bond_level_5, BondEngine.titleResFor(5))
        assertEquals(R.string.bond_level_5, BondEngine.titleResFor(9))
    }

    @Test
    fun `Accessoires ab Level 2`() {
        assertEquals("", BondEngine.accessoryFor(1))
        assertEquals("🎀", BondEngine.accessoryFor(2))
        assertEquals("⭐", BondEngine.accessoryFor(3))
        assertEquals("🎩", BondEngine.accessoryFor(4))
        assertEquals("👑", BondEngine.accessoryFor(5))
    }

    @Test
    fun `Check-in-Bonus nur alle 30 Minuten`() {
        val now = 1_000_000L
        assertEquals(BondEngine.BOND_PER_CHECK_IN, BondEngine.checkInBonus(0L, now))
        assertEquals(0, BondEngine.checkInBonus(now - 10 * minute, now))
        assertEquals(BondEngine.BOND_PER_CHECK_IN, BondEngine.checkInBonus(now - 31 * minute, now))
    }
}
