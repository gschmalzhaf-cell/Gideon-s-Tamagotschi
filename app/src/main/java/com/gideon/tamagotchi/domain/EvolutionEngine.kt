package com.gideon.tamagotchi.domain

import com.gideon.tamagotchi.domain.model.PetStage

object EvolutionEngine {

    private const val DAY_MILLIS = 24L * 60 * 60 * 1000

    private const val CHILD_AFTER_DAYS = 1.0
    private const val TEEN_AFTER_DAYS = 3.0
    private const val ADULT_AFTER_DAYS = 7.0

    private const val LOW_HEALTH = 30

    fun stageFor(createdAt: Long, health: Int, now: Long): PetStage {
        val ageDays = (now - createdAt).toDouble() / DAY_MILLIS
        val base = when {
            ageDays < CHILD_AFTER_DAYS -> PetStage.BABY
            ageDays < TEEN_AFTER_DAYS -> PetStage.CHILD
            ageDays < ADULT_AFTER_DAYS -> PetStage.TEEN
            else -> PetStage.ADULT
        }
        return if (health < LOW_HEALTH && base.ordinal > PetStage.BABY.ordinal) {
            PetStage.entries[base.ordinal - 1]
        } else {
            base
        }
    }
}
