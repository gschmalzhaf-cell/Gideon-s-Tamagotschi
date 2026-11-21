package com.gideon.tamagotchi.domain.model

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
    val lastTickAt: Long = 0
) {
    val health: Int
        get() = (hunger + happiness + energy + hygiene) / 4

    val stage: PetStage
        get() = EvolutionEngine.stageFor(createdAt, health, System.currentTimeMillis())
}
