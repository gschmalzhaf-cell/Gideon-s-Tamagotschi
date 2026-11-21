package com.gideon.tamagotchi.ui.create

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gideon.tamagotchi.R
import com.gideon.tamagotchi.domain.model.PetStage
import com.gideon.tamagotchi.domain.model.PetType
import com.gideon.tamagotchi.ui.components.PetCharacterCanvas
import com.gideon.tamagotchi.ui.components.PetMood
import kotlinx.coroutines.launch

private val petColors = listOf(
    0xFFE0316EL, // Gideon Berry Pink
    0xFFFF7A3DL, // Tangerine Peach
    0xFFF4C430L, // Sunny Gold
    0xFF2F9E44L, // Mint Emerald
    0xFF0EA5E9L, // Sky Aqua
    0xFF6366F1L, // Cosmic Indigo
    0xFF9C5EC2L, // Pastel Lavender
    0xFFFF6B8BL  // Strawberry Coral
)

private val suggestedNames = listOf(
    "Gideon", "Mochi", "Krümel", "Pixel", "Pudding", "Flocke", "Bloop", "Sternchen", "Keks", "Nori"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePetScreen(
    onPetCreated: () -> Unit,
    viewModel: CreatePetViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PetType.BLOB) }
    var selectedColor by remember { mutableStateOf(petColors.first()) }
    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            Color(selectedColor).copy(alpha = 0.16f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.22f)
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
                            text = stringResource(id = R.string.create_title),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = stringResource(id = R.string.create_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Live Interactive Pet Hatchery Preview Card
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    tonalElevation = 4.dp,
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color(selectedColor).copy(alpha = 0.45f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(selectedColor).copy(alpha = 0.18f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PetCharacterCanvas(
                                type = selectedType,
                                stage = PetStage.BABY,
                                colorHex = selectedColor,
                                mood = PetMood.HAPPY,
                                size = 152.dp
                            )
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(selectedColor).copy(alpha = 0.14f)
                            ) {
                                Text(
                                    text = if (name.isBlank()) {
                                        "🥚 ${stringResource(id = selectedType.labelRes)}"
                                    } else {
                                        "✨ ${name.trim()}"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // Name Input + Dice Randomizer
                OutlinedTextField(
                    value = name,
                    onValueChange = { if (it.length <= 20) name = it },
                    label = { Text(text = stringResource(id = R.string.create_name_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.create_name_placeholder)) },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                val candidates = suggestedNames.filter { it != name }
                                name = candidates.random()
                            }
                        ) {
                            Text(text = "🎲", fontSize = 20.sp)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Visual Species Selector Cards
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.create_type_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PetType.entries.forEach { type ->
                            val selected = type == selectedType
                            val cardScale by animateFloatAsState(
                                targetValue = if (selected) 1.03f else 0.98f,
                                animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
                                label = "type_scale"
                            )
                            Surface(
                                onClick = { selectedType = type },
                                shape = RoundedCornerShape(20.dp),
                                color = if (selected) {
                                    Color(selectedColor).copy(alpha = 0.16f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                                border = BorderStroke(
                                    width = if (selected) 2.5.dp else 1.dp,
                                    color = if (selected) {
                                        Color(selectedColor)
                                    } else {
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                                    }
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .scale(cardScale)
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    PetCharacterCanvas(
                                        type = type,
                                        stage = PetStage.BABY,
                                        colorHex = selectedColor,
                                        mood = if (selected) PetMood.HAPPY else PetMood.NEUTRAL,
                                        size = 62.dp
                                    )
                                    Text(
                                        text = stringResource(id = type.labelRes),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // Expanded Color Swatch Picker (2 rows of 4)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.create_color_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                    petColors.chunked(4).forEach { rowColors ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowColors.forEach { hex ->
                                val isSelected = hex == selectedColor
                                val swatchScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.16f else 1f,
                                    animationSpec = spring(dampingRatio = 0.5f, stiffness = 450f),
                                    label = "color_scale"
                                )
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .scale(swatchScale)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                        .then(
                                            if (isSelected) {
                                                Modifier.border(
                                                    width = 3.5.dp,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    shape = CircleShape
                                                )
                                            } else {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = Color.White.copy(alpha = 0.65f),
                                                    shape = CircleShape
                                                )
                                            }
                                        )
                                        .clickable { selectedColor = hex },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Text(
                                            text = "✓",
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Hatch CTA Button
                Button(
                    onClick = {
                        scope.launch {
                            isSaving = true
                            viewModel.createPet(name, selectedType, selectedColor)
                            isSaving = false
                            onPetCreated()
                        }
                    },
                    enabled = name.isNotBlank() && !isSaving,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(selectedColor),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "🐣 ${stringResource(id = R.string.create_submit)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
