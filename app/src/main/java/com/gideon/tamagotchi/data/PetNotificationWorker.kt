package com.gideon.tamagotchi.data

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gideon.tamagotchi.GideonApplication
import com.gideon.tamagotchi.MainActivity
import com.gideon.tamagotchi.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PetNotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val petRepository: PetRepository,
    private val settingsRepository: SettingsRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (!settingsRepository.settings.value.notificationsEnabled) return Result.success()
        if (Build.VERSION.SDK_INT >= 33 &&
            appContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }

        val pet = petRepository.getPet() ?: return Result.success()
        if (!pet.isAlive) return Result.success()

        val (titleRes, textRes) = when {
            pet.hunger < 30 -> R.string.notif_hunger_title to R.string.notif_hunger_text
            pet.health < 30 -> R.string.notif_health_title to R.string.notif_health_text
            pet.happiness < 30 -> R.string.notif_miss_title to R.string.notif_miss_text
            else -> return Result.success()
        }

        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(appContext, GideonApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(appContext.getString(titleRes, pet.name))
            .setContentText(appContext.getString(textRes, pet.name))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(appContext).notify(pet.id.toInt(), notification)
        return Result.success()
    }

    companion object {
        const val NAME = "pet_notifications"
    }
}
