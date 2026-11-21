package com.gideon.tamagotchi.domain.model

import com.gideon.tamagotchi.R

enum class PetAction(val labelRes: Int) {
    FEED(R.string.action_feed),
    PLAY(R.string.action_play),
    WASH(R.string.action_wash),
    SLEEP(R.string.action_sleep),
    MEDICINE(R.string.action_medicine)
}
