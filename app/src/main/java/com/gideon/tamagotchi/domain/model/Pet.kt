package com.gideon.tamagotchi.domain.model

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
    val isAlive: Boolean
) {
    val health: Int
        get() = (hunger + happiness + energy + hygiene) / 4
}
