package com.gideon.tamagotchi

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gideon.tamagotchi.data.PetTickWorker
import com.gideon.tamagotchi.data.PetTicker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class GideonApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var petTicker: PetTicker

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        petTicker.start()
        scheduleBackgroundTicks()
    }

    private fun scheduleBackgroundTicks() {
        val request = PeriodicWorkRequestBuilder<PetTickWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            PetTickWorker.NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
