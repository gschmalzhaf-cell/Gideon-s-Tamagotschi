package com.gideon.tamagotchi.domain.model

import com.gideon.tamagotchi.R

enum class PetType(val labelRes: Int) {
    BLOB(R.string.type_blob),
    COOKIE(R.string.type_cookie),
    STAR(R.string.type_star)
}
