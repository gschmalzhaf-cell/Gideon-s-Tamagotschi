package com.gideon.tamagotchi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gideon.tamagotchi.data.PetNotificationWorker
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
        createNotificationChannel()
        petTicker.start()
        scheduleBackgroundTicks()
        scheduleNotifications()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notif_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun scheduleBackgroundTicks() {
        val request = PeriodicWorkRequestBuilder<PetTickWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            PetTickWorker.NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private fun scheduleNotifications() {
        val request = PeriodicWorkRequestBuilder<PetNotificationWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            PetNotificationWorker.NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    companion object {
        const val CHANNEL_ID = "pet_alerts"
    }
}
