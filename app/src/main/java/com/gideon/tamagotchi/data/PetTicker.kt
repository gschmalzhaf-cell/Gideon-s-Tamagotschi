package com.gideon.tamagotchi.data

import com.gideon.tamagotchi.domain.TickCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetTicker @Inject constructor(
    private val petRepository: PetRepository,
    private val appScope: CoroutineScope
) {
    fun start() {
        appScope.launch {
            while (isActive) {
                delay(TimeUnit.MINUTES.toMillis(TickCalculator.TICK_INTERVAL_MINUTES))
                petRepository.applyElapsedTicks()
            }
        }
    }
}
