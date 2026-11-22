package com.gideon.tamagotchi.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PetTickWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val petRepository: PetRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        petRepository.applyElapsedTicks()
        return Result.success()
    }

    companion object {
        const val NAME = "pet_tick"
    }
}
