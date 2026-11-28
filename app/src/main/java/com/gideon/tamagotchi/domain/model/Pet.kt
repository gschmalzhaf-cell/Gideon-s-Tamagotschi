package com.gideon.tamagotchi.domain.model

import com.gideon.tamagotchi.domain.BondEngine
import com.gideon.tamagotchi.domain.EvolutionEngine

data class Pet(
    val id: Long,
    val name: String,
    val type: PetType,
    val colorHex: Long,
    val createdAt: Long,
    val hunger: Int,
    val happiness: Int,
    val energy: Int,
    val hygiene: Int,
    val isAlive: Boolean,
    val isSleeping: Boolean = false,
    val lastTickAt: Long = 0,
    val bond: Int = 0,
    val lastCheckInAt: Long = 0
) {
    val health: Int
        get() = (hunger + happiness + energy + hygiene) / 4

    val stage: PetStage
        get() = EvolutionEngine.stageFor(createdAt, health, System.currentTimeMillis())

    val bondLevel: Int
        get() = BondEngine.level(bond)

    val bondTitleRes: Int
        get() = BondEngine.titleResFor(bondLevel)

    val accessory: String
        get() = BondEngine.accessoryFor(bondLevel)

    val bondProgressInLevel: Int
        get() = bond % BondEngine.BOND_PER_LEVEL
}
