package com.gideon.tamagotchi.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gideon.tamagotchi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNoPet: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoading, uiState.pet) {
        if (!uiState.isLoading && uiState.pet == null) {
            onNoPet()
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = pet.name) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(pet.colorHex)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = pet.type.labelRes),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }

            StatBar(label = stringResource(id = R.string.stat_hunger), value = pet.hunger)
            StatBar(label = stringResource(id = R.string.stat_happiness), value = pet.happiness)
            StatBar(label = stringResource(id = R.string.stat_energy), value = pet.energy)
            StatBar(label = stringResource(id = R.string.stat_hygiene), value = pet.hygiene)
            StatBar(label = stringResource(id = R.string.stat_health), value = pet.health)
        }
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
