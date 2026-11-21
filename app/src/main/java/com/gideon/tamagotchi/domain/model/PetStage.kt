package com.gideon.tamagotchi.domain.model

import com.gideon.tamagotchi.R

enum class PetStage(val labelRes: Int) {
    BABY(R.string.stage_baby),
    CHILD(R.string.stage_child),
    TEEN(R.string.stage_teen),
    ADULT(R.string.stage_adult)
}
