package com.gideon.tamagotchi.domain

import com.gideon.tamagotchi.R

object BondEngine {

    const val BOND_PER_ACTION = 5
    const val BOND_PER_CHECK_IN = 2
    const val CHECK_IN_COOLDOWN_MINUTES = 30L
    const val BOND_PER_LEVEL = 100

    fun level(bond: Int): Int = bond / BOND_PER_LEVEL + 1

    fun titleResFor(level: Int): Int = when (level) {
        1 -> R.string.bond_level_1
        2 -> R.string.bond_level_2
        3 -> R.string.bond_level_3
        4 -> R.string.bond_level_4
        else -> R.string.bond_level_5
    }

    fun accessoryFor(level: Int): String = when {
        level >= 5 -> "👑"
        level >= 4 -> "🎩"
        level >= 3 -> "⭐"
        level >= 2 -> "🎀"
        else -> ""
    }

    fun checkInBonus(lastCheckInAt: Long, now: Long): Int =
        if (lastCheckInAt == 0L || now - lastCheckInAt >= CHECK_IN_COOLDOWN_MINUTES * 60_000L) {
            BOND_PER_CHECK_IN
        } else {
            0
        }
}
