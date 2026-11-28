package com.gideon.tamagotchi.data

import com.gideon.tamagotchi.data.local.PetEntity
import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetType

fun PetEntity.toDomain(): Pet = Pet(
    id = id,
    name = name,
    type = PetType.valueOf(type),
    colorHex = colorHex,
    createdAt = createdAt,
    hunger = hunger,
    happiness = happiness,
    energy = energy,
    hygiene = hygiene,
    isAlive = isAlive,
    isSleeping = isSleeping,
    lastTickAt = lastTickAt,
    bond = bond,
    lastCheckInAt = lastCheckInAt
)

fun Pet.toEntity(): PetEntity = PetEntity(
    id = id,
    name = name,
    type = type.name,
    colorHex = colorHex,
    createdAt = createdAt,
    hunger = hunger,
    happiness = happiness,
    energy = energy,
    hygiene = hygiene,
    isAlive = isAlive,
    isSleeping = isSleeping,
    lastTickAt = lastTickAt,
    bond = bond,
    lastCheckInAt = lastCheckInAt
)
