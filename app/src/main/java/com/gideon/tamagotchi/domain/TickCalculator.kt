package com.gideon.tamagotchi.domain

import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetAction

object TickCalculator {

    const val TICK_INTERVAL_MINUTES = 1L

    private const val HUNGER_DECAY_PER_MIN = 1
    private const val SLEEP_HUNGER_DECAY_PER_MIN = 2
    private const val HYGIENE_DECAY_PER_MIN = 1
    private const val HAPPINESS_DECAY_PER_MIN = 1
    private const val ENERGY_DECAY_PER_MIN = 1
    private const val ENERGY_REGEN_PER_MIN = 10

    private const val FEED_AMOUNT = 25
    private const val PLAY_HAPPINESS = 20
    private const val PLAY_ENERGY_COST = 10
    private const val WASH_AMOUNT = 30
    private const val MEDICINE_AMOUNT = 25

    private const val SICK_HEALTH_THRESHOLD = 30

    fun decay(pet: Pet, minutes: Long): Pet {
        if (!pet.isAlive || minutes <= 0) return pet

        // Kranke Pets (Gesundheit < 30) verfallen nur halb so schnell -> verzeihenderes Balancing
        val effectiveMinutes = if (pet.health < SICK_HEALTH_THRESHOLD) minutes / 2 else minutes

        val hungerDecay = if (pet.isSleeping) SLEEP_HUNGER_DECAY_PER_MIN else HUNGER_DECAY_PER_MIN
        var updated = pet.copy(
            hunger = clamp(pet.hunger - effectiveMinutes * hungerDecay),
            hygiene = clamp(pet.hygiene - effectiveMinutes * HYGIENE_DECAY_PER_MIN),
            happiness = clamp(pet.happiness - effectiveMinutes * HAPPINESS_DECAY_PER_MIN),
            energy = if (pet.isSleeping) {
                clamp(pet.energy + effectiveMinutes * ENERGY_REGEN_PER_MIN)
            } else {
                clamp(pet.energy - effectiveMinutes * ENERGY_DECAY_PER_MIN)
            }
        )

        if (updated.isSleeping && updated.energy >= 100) {
            updated = updated.copy(isSleeping = false)
        }
        if (updated.health <= 0) {
            updated = updated.copy(isAlive = false, isSleeping = false)
        }
        return updated
    }

    fun applyAction(pet: Pet, action: PetAction): Pet {
        if (!pet.isAlive) return pet
        return when (action) {
            PetAction.FEED -> pet.copy(hunger = clamp(pet.hunger + FEED_AMOUNT.toLong()))
            PetAction.PLAY -> pet.copy(
                happiness = clamp(pet.happiness + PLAY_HAPPINESS.toLong()),
                energy = clamp(pet.energy - PLAY_ENERGY_COST.toLong())
            )

            PetAction.WASH -> pet.copy(hygiene = clamp(pet.hygiene + WASH_AMOUNT.toLong()))
            PetAction.SLEEP -> pet.copy(isSleeping = !pet.isSleeping)
            PetAction.MEDICINE -> {
                val lowest = minOf(pet.hunger, pet.happiness, pet.energy, pet.hygiene)
                when (lowest) {
                    pet.hunger -> pet.copy(hunger = clamp(pet.hunger + MEDICINE_AMOUNT.toLong()))
                    pet.happiness -> pet.copy(happiness = clamp(pet.happiness + MEDICINE_AMOUNT.toLong()))
                    pet.energy -> pet.copy(energy = clamp(pet.energy + MEDICINE_AMOUNT.toLong()))
                    else -> pet.copy(hygiene = clamp(pet.hygiene + MEDICINE_AMOUNT.toLong()))
                }
            }
        }
    }

    private fun clamp(value: Long): Int = value.coerceIn(0L, 100L).toInt()
}
