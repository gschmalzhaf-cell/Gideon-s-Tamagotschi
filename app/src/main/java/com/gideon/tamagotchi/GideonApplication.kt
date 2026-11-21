package com.gideon.tamagotchi

import android.app.Application
import com.gideon.tamagotchi.data.PetTicker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GideonApplication : Application() {

    @Inject
    lateinit var petTicker: PetTicker

    override fun onCreate() {
        super.onCreate()
        petTicker.start()
    }
}
