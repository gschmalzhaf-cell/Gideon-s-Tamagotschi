package com.gideon.tamagotchi.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gideon.tamagotchi.R
import com.gideon.tamagotchi.data.OfflineReport
import com.gideon.tamagotchi.domain.model.PetAction

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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (offlineReport != null) {
        val report = offlineReport!!
        AlertDialog(
            onDismissRequest = { offlineReport = null },
            title = { Text(text = stringResource(id = R.string.offline_title)) },
            text = {
                Text(text = stringResource(id = R.string.offline_message, report.minutes))
            },
            confirmButton = {
                TextButton(onClick = { offlineReport = null }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            }
        )
    }

    val pulseTransition = rememberInfiniteTransition(label = "pet_pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (pet.isAlive) 1.06f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (pet.isSleeping) 1600 else 900,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = pet.name) },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.settings_open)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(if (pet.isAlive) Color(pet.colorHex) else Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = pet.type.labelRes),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }

            Text(
                text = stringResource(id = pet.stage.labelRes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            if (pet.isSleeping) {
                Text(
                    text = stringResource(id = R.string.pet_sleeping, pet.name),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            StatBar(label = stringResource(id = R.string.stat_hunger), value = pet.hunger)
            StatBar(label = stringResource(id = R.string.stat_happiness), value = pet.happiness)
            StatBar(label = stringResource(id = R.string.stat_energy), value = pet.energy)
            StatBar(label = stringResource(id = R.string.stat_hygiene), value = pet.hygiene)
            StatBar(label = stringResource(id = R.string.stat_health), value = pet.health)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionButton(
                        label = stringResource(id = PetAction.FEED.labelRes),
                        enabled = pet.isAlive,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.onAction(PetAction.FEED) }
                    ActionButton(
                        label = stringResource(id = PetAction.PLAY.labelRes),
                        enabled = pet.isAlive,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.onAction(PetAction.PLAY) }
                    ActionButton(
                        label = stringResource(id = PetAction.WASH.labelRes),
                        enabled = pet.isAlive,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.onAction(PetAction.WASH) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionButton(
                        label = stringResource(
                            id = if (pet.isSleeping) R.string.action_wake else PetAction.SLEEP.labelRes
                        ),
                        enabled = pet.isAlive,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.onAction(PetAction.SLEEP) }
                    ActionButton(
                        label = stringResource(id = PetAction.MEDICINE.labelRes),
                        enabled = pet.isAlive,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.onAction(PetAction.MEDICINE) }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text = label)
    }
}

@Composable
private fun StatBar(label: String, value: Int) {
    val color = when {
        value < 30 -> MaterialTheme.colorScheme.error
        value < 60 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        LinearProgressIndicator(
            progress = { value / 100f },
            modifier = Modifier.fillMaxWidth(),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
