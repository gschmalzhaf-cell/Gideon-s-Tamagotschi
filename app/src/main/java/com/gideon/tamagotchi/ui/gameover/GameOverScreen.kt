package com.gideon.tamagotchi.ui.gameover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gideon.tamagotchi.R
import com.gideon.tamagotchi.domain.model.PetStage
import com.gideon.tamagotchi.domain.model.PetType
import com.gideon.tamagotchi.ui.components.PetCharacterCanvas
import com.gideon.tamagotchi.ui.components.PetMood
import com.gideon.tamagotchi.ui.theme.GideonColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameOverScreen(
    onNewPet: () -> Unit,
    viewModel: GameOverViewModel = hiltViewModel()
) {
    val pet by viewModel.pet.collectAsStateWithLifecycle()

    val nightGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF141126),
            Color(0xFF23193A),
            Color(0xFF181224)
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
                    Text(
                        text = stringResource(id = R.string.game_over_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFFFE4EC)
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(nightGradient)
        ) {
            // Starry memorial sky backdrop
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stars = listOf(
                    Offset(0.12f, 0.14f), Offset(0.28f, 0.22f), Offset(0.48f, 0.12f),
                    Offset(0.76f, 0.18f), Offset(0.88f, 0.28f), Offset(0.18f, 0.42f),
                    Offset(0.82f, 0.46f), Offset(0.32f, 0.62f), Offset(0.70f, 0.68f)
                )
                stars.forEachIndexed { idx, p ->
                    drawCircle(
                        color = Color(0xFFFFF8DC).copy(alpha = if (idx % 2 == 0) 0.7f else 0.4f),
                        radius = (1.8f + (idx % 3) * 1.1f).dp.toPx(),
                        center = Offset(size.width * p.x, size.height * p.y)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Spacer(modifier = Modifier.weight(0.6f))

                // Floating Ghost Pet with Golden Angel Halo
                PetCharacterCanvas(
                    type = pet?.type ?: PetType.BLOB,
                    stage = pet?.stage ?: PetStage.BABY,
                    colorHex = pet?.colorHex ?: 0xFFB4B8C4L,
                    mood = PetMood.DEAD,
                    size = 184.dp
                )

                // Memorial Card
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0xFF2B2042).copy(alpha = 0.88f),
                    border = BorderStroke(1.5.dp, Color(0xFF6B5399).copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.game_over_message, pet?.name.orEmpty()),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFFFF8FA3),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.game_over_memorial),
                            style = MaterialTheme.typography.titleSmall,
                            color = GideonColors.GoldCrown,
                            textAlign = TextAlign.Center
                        )
                        if (pet != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.08f)
                            ) {
                                Text(
                                    text = stringResource(
                                        id = R.string.game_over_stage_reached,
                                        stringResource(id = pet!!.stage.labelRes)
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFFE0D7F5),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                        Text(
                            text = stringResource(id = R.string.game_over_reason),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFCBBFD4),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onNewPet,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "🥚 ${stringResource(id = R.string.game_over_new_pet)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
