package com.gideon.tamagotchi.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gideon.tamagotchi.R
import com.gideon.tamagotchi.data.OfflineReport
import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetAction
import com.gideon.tamagotchi.ui.components.CompactStatCard
import com.gideon.tamagotchi.ui.components.HabitatEffect
import com.gideon.tamagotchi.ui.components.HabitatEffectEvent
import com.gideon.tamagotchi.ui.components.HealthMasterBar
import com.gideon.tamagotchi.ui.components.TactileActionButton
import com.gideon.tamagotchi.ui.components.TamagotchiHabitatShell
import com.gideon.tamagotchi.ui.components.computePetMood
import com.gideon.tamagotchi.ui.theme.GideonColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNoPet: () -> Unit,
    onPetDied: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoading, uiState.pet) {
        val currentPet = uiState.pet
        when {
            !uiState.isLoading && currentPet == null -> onNoPet()
            !uiState.isLoading && currentPet != null && !currentPet.isAlive -> onPetDied()
        }
    }

    var offlineReport by remember { mutableStateOf<OfflineReport?>(null) }
    LaunchedEffect(Unit) {
        viewModel.offlineReport.collect { report ->
            if (!report.diedDuringAbsence) {
                offlineReport = report
            }
        }
    }

    val pet = uiState.pet
    if (pet == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    var effectCounter by remember { mutableLongStateOf(0L) }
    var effectEvent by remember { mutableStateOf<HabitatEffectEvent?>(null) }
    var pettedBanner by remember { mutableStateOf(false) }

    LaunchedEffect(pettedBanner) {
        if (pettedBanner) {
            delay(1800L)
            pettedBanner = false
        }
    }

    fun triggerEffect(effect: HabitatEffect) {
        effectCounter += 1
        effectEvent = HabitatEffectEvent(id = effectCounter, effect = effect)
    }

    if (offlineReport != null) {
        val report = offlineReport!!
        AlertDialog(
            onDismissRequest = { offlineReport = null },
            shape = RoundedCornerShape(26.dp),
            icon = { Text(text = "🌙", fontSize = 32.sp) },
            title = {
                Text(
                    text = stringResource(id = R.string.offline_title),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.offline_message, report.minutes),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(onClick = { offlineReport = null }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            }
        )
    }

    val mood = computePetMood(pet)
    val speechText = if (pettedBanner) {
        stringResource(id = R.string.mood_loved)
    } else {
        stringResource(id = mood.messageRes)
    }

    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = pet.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        tonalElevation = 2.dp,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(
                            onClick = onOpenSettings,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(id = R.string.settings_open),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Retro Tamagotchi Pocket Console Viewport
                TamagotchiHabitatShell(
                    pet = pet,
                    mood = mood,
                    speechText = speechText,
                    effectEvent = effectEvent,
                    onPetTap = {
                        if (pet.isAlive && !pet.isSleeping) {
                            pettedBanner = true
                            triggerEffect(HabitatEffect.LOVE)
                        }
                    }
                )

                // Master Health Bar
                HealthMasterBar(
                    label = stringResource(id = R.string.stat_health),
                    value = pet.health
                )

                // Bond / Beziehung: XP durch Pflege & Check-ins, Level schalten Accessoires frei
                BondSection(pet = pet)

                // 2x2 Core Needs Grid
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CompactStatCard(
                            emoji = "🍔",
                            label = stringResource(id = R.string.stat_hunger),
                            value = pet.hunger,
                            accentColor = GideonColors.StatHunger,
                            modifier = Modifier.weight(1f)
                        )
                        CompactStatCard(
                            emoji = "✨",
                            label = stringResource(id = R.string.stat_happiness),
                            value = pet.happiness,
                            accentColor = GideonColors.StatHappiness,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CompactStatCard(
                            emoji = "⚡",
                            label = stringResource(id = R.string.stat_energy),
                            value = pet.energy,
                            accentColor = GideonColors.StatEnergy,
                            modifier = Modifier.weight(1f)
                        )
                        CompactStatCard(
                            emoji = "🫧",
                            label = stringResource(id = R.string.stat_hygiene),
                            value = pet.hygiene,
                            accentColor = GideonColors.StatHygiene,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Chunky Tactile Arcade Action Deck
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TactileActionButton(
                            emoji = "🍔",
                            label = stringResource(id = PetAction.FEED.labelRes),
                            accentColor = GideonColors.ActionFeed,
                            enabled = pet.isAlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            triggerEffect(HabitatEffect.FEED)
                            viewModel.onAction(PetAction.FEED)
                        }
                        TactileActionButton(
                            emoji = "🎾",
                            label = stringResource(id = PetAction.PLAY.labelRes),
                            accentColor = GideonColors.ActionPlay,
                            enabled = pet.isAlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            triggerEffect(HabitatEffect.PLAY)
                            viewModel.onAction(PetAction.PLAY)
                        }
                        TactileActionButton(
                            emoji = "🛁",
                            label = stringResource(id = PetAction.WASH.labelRes),
                            accentColor = GideonColors.ActionWash,
                            enabled = pet.isAlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            triggerEffect(HabitatEffect.WASH)
                            viewModel.onAction(PetAction.WASH)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TactileActionButton(
                            emoji = if (pet.isSleeping) "☀️" else "😴",
                            label = stringResource(
                                id = if (pet.isSleeping) R.string.action_wake else PetAction.SLEEP.labelRes
                            ),
                            accentColor = GideonColors.ActionSleep,
                            enabled = pet.isAlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            triggerEffect(HabitatEffect.SLEEP)
                            viewModel.onAction(PetAction.SLEEP)
                        }
                        TactileActionButton(
                            emoji = "💊",
                            label = stringResource(id = PetAction.MEDICINE.labelRes),
                            accentColor = GideonColors.ActionMedicine,
                            enabled = pet.isAlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            triggerEffect(HabitatEffect.MEDICINE)
                            viewModel.onAction(PetAction.MEDICINE)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun BondSection(pet: Pet) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💞 ${stringResource(id = R.string.bond_label)} – ${stringResource(id = pet.bondTitleRes)}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${pet.accessory} Lv ${pet.bondLevel}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LinearProgressIndicator(
                progress = { pet.bondProgressInLevel / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
