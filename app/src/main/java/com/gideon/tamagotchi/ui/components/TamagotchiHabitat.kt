package com.gideon.tamagotchi.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gideon.tamagotchi.R
import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetStage
import com.gideon.tamagotchi.ui.theme.GideonColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

enum class HabitatEffect(val emojis: List<String>) {
    FEED(listOf("🍔", "🍓", "🍰", "✨")),
    PLAY(listOf("🎾", "⭐", "🎉", "✨")),
    WASH(listOf("🫧", "💧", "✨", "🧼")),
    SLEEP(listOf("🌙", "💤", "⭐", "✨")),
    MEDICINE(listOf("💊", "💚", "✨", "🩺")),
    LOVE(listOf("💖", "💕", "✨", "🥰"))
}

data class HabitatEffectEvent(
    val id: Long,
    val effect: HabitatEffect
)

@Composable
fun TamagotchiHabitatShell(
    pet: Pet,
    mood: PetMood,
    speechText: String,
    effectEvent: HabitatEffectEvent?,
    onPetTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val shellBg = if (isDark) GideonColors.ShellPinkDark else GideonColors.ShellPinkLight
    val bezelColor = if (isDark) GideonColors.ShellBezelDark else GideonColors.ShellBezelLight

    val ledColor = when {
        !pet.isAlive -> Color.Gray
        pet.isSleeping -> GideonColors.ActionSleep
        pet.health < 30 -> GideonColors.StatCritical
        pet.health < 60 -> GideonColors.StatWarning
        else -> GideonColors.StatHealth
    }

    val infinite = rememberInfiniteTransition(label = "shell_led")
    val ledAlpha by infinite.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (pet.health < 30) 450 else 1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "led_alpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(32.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(32.dp),
        color = shellBg,
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = bezelColor.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Handheld Console Top Strip (LED + Model Label + Stage Pill)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(ledColor.copy(alpha = ledAlpha))
                            .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                    )
                    Text(
                        text = "GIDEON-OS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StagePill(stage = pet.stage)
                    AgePill(createdAt = pet.createdAt)
                }
            }

            // Inner LCD Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(232.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(
                        width = 3.dp,
                        color = bezelColor,
                        shape = RoundedCornerShape(22.dp)
                    )
            ) {
                HabitatBackdrop(
                    mood = mood,
                    modifier = Modifier.fillMaxSize()
                )

                // Pet Character centered in habitat
                PetCharacterCanvas(
                    type = pet.type,
                    stage = pet.stage,
                    colorHex = pet.colorHex,
                    mood = mood,
                    size = 164.dp,
                    onTap = onPetTap,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 6.dp)
                )

                // Action / Love Particle Burst Overlay
                if (effectEvent != null) {
                    ParticleBurstOverlay(
                        event = effectEvent,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Floating Speech / Mood Bubble at top of LCD
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp)
                ) {
                    AnimatedContent(
                        targetState = speechText,
                        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(180)) },
                        label = "mood_speech"
                    ) { text ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = if (mood == PetMood.SLEEPING) 0.18f else 0.86f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (mood == PetMood.SLEEPING) Color.White.copy(alpha = 0.25f) else Color.White
                            )
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (mood == PetMood.SLEEPING) Color(0xFFE9E6FF) else Color(0xFF2B1E28),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // Subtle tap hint at bottom of handheld shell
            if (pet.isAlive && !pet.isSleeping) {
                Text(
                    text = stringResource(id = R.string.pet_tap_hint, pet.name),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun StagePill(stage: PetStage) {
    val emoji = when (stage) {
        PetStage.BABY -> "🐣"
        PetStage.CHILD -> "🌱"
        PetStage.TEEN -> "🎀"
        PetStage.ADULT -> "👑"
    }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = "$emoji ${stringResource(id = stage.labelRes)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun AgePill(createdAt: Long) {
    val elapsedMs = (System.currentTimeMillis() - createdAt).coerceAtLeast(0L)
    val hours = elapsedMs / (1000L * 60L * 60L)
    val days = hours / 24L
    val label = when {
        hours < 1L -> stringResource(id = R.string.pet_age_new)
        days < 1L -> stringResource(id = R.string.pet_age_hours, hours)
        days == 1L -> stringResource(id = R.string.pet_age_days_one)
        else -> stringResource(id = R.string.pet_age_days, days)
    }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun HabitatBackdrop(
    mood: PetMood,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "habitat_bg")
    val drift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloud_drift"
    )
    val twinkle by infinite.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val (skyTop, skyBottom, hillBack, hillFront) = when (mood) {
            PetMood.SLEEPING, PetMood.DEAD -> listOf(
                GideonColors.LcdSkyNightTop,
                GideonColors.LcdSkyNightBottom,
                GideonColors.LcdGrassNightBack,
                GideonColors.LcdGrassNight
            )

            PetMood.SICK -> listOf(
                GideonColors.LcdSkySickTop,
                GideonColors.LcdSkySickBottom,
                Color(0xFF83998B),
                GideonColors.LcdGrassSick
            )

            else -> listOf(
                GideonColors.LcdSkyDayTop,
                GideonColors.LcdSkyDayBottom,
                GideonColors.LcdGrassDayBack,
                GideonColors.LcdGrassDay
            )
        }

        // Sky gradient
        drawRect(
            brush = Brush.verticalGradient(colors = listOf(skyTop, skyBottom))
        )

        if (mood == PetMood.SLEEPING || mood == PetMood.DEAD) {
            // Crescent moon
            val moonCenter = Offset(w * 0.82f, h * 0.22f)
            drawCircle(color = Color(0xFFFFF3B0), radius = 18.dp.toPx(), center = moonCenter)
            drawCircle(
                color = skyTop,
                radius = 15.dp.toPx(),
                center = Offset(moonCenter.x + 7.dp.toPx(), moonCenter.y - 4.dp.toPx())
            )
            // Twinkling night stars
            val stars = listOf(
                Offset(0.14f, 0.20f), Offset(0.28f, 0.14f), Offset(0.45f, 0.24f),
                Offset(0.64f, 0.16f), Offset(0.20f, 0.36f), Offset(0.74f, 0.35f)
            )
            stars.forEachIndexed { i, pos ->
                val a = if (i % 2 == 0) twinkle else 1.25f - twinkle
                drawCircle(
                    color = Color(0xFFFFF9DB).copy(alpha = a.coerceIn(0.2f, 1f)),
                    radius = (2.2f + (i % 2) * 1.2f).dp.toPx(),
                    center = Offset(w * pos.x, h * pos.y)
                )
            }
        } else {
            // Soft drifting pastel clouds
            val cloud1X = ((drift * (w + 160f)) - 80f)
            val cloud2X = (((drift + 0.52f) % 1f) * (w + 160f) - 80f)
            val cloudColor = if (mood == PetMood.SICK) Color(0xFFE2E8F0).copy(alpha = 0.7f) else Color.White.copy(alpha = 0.85f)
            drawFluffyCloud(Offset(cloud1X, h * 0.22f), 26.dp.toPx(), cloudColor)
            drawFluffyCloud(Offset(cloud2X, h * 0.34f), 20.dp.toPx(), cloudColor.copy(alpha = 0.7f))

            // Subtle happy sparkles
            if (mood == PetMood.HAPPY) {
                val sparkles = listOf(Offset(0.16f, 0.28f), Offset(0.84f, 0.26f), Offset(0.78f, 0.50f))
                sparkles.forEachIndexed { idx, p ->
                    val alpha = if (idx % 2 == 0) twinkle else 1.2f - twinkle
                    drawCircle(
                        color = Color(0xFFFFD43B).copy(alpha = alpha.coerceIn(0.2f, 0.9f)),
                        radius = 3.dp.toPx(),
                        center = Offset(w * p.x, h * p.y)
                    )
                }
            }
        }

        // Back rolling hill
        val backHill = Path().apply {
            moveTo(0f, h * 0.76f)
            quadraticTo(w * 0.28f, h * 0.62f, w * 0.58f, h * 0.74f)
            quadraticTo(w * 0.82f, h * 0.82f, w, h * 0.70f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(backHill, color = hillBack)

        // Front rolling meadow
        val frontHill = Path().apply {
            moveTo(0f, h * 0.82f)
            quadraticTo(w * 0.5f, h * 0.72f, w, h * 0.84f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(frontHill, color = hillFront)

        // Tiny meadow flowers during daytime
        if (mood != PetMood.SLEEPING && mood != PetMood.DEAD && mood != PetMood.SICK) {
            val flowers = listOf(
                Triple(0.14f, 0.88f, Color(0xFFFF85A1)),
                Triple(0.24f, 0.92f, Color(0xFFFFE066)),
                Triple(0.79f, 0.90f, Color(0xFFFF85A1)),
                Triple(0.89f, 0.87f, Color(0xFFFFFFFF))
            )
            flowers.forEach { (fx, fy, fColor) ->
                drawCircle(color = fColor, radius = 3.5.dp.toPx(), center = Offset(w * fx, h * fy))
                drawCircle(color = Color(0xFFFFD43B), radius = 1.5.dp.toPx(), center = Offset(w * fx, h * fy))
            }
        }

        // Subtle retro LCD scanline texture
        val step = 6.dp.toPx()
        var y = 0f
        while (y < h) {
            drawLine(
                color = Color.Black.copy(alpha = 0.025f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1f
            )
            y += step
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFluffyCloud(
    center: Offset,
    radius: Float,
    color: Color
) {
    drawCircle(color = color, radius = radius * 0.72f, center = Offset(center.x - radius * 0.65f, center.y + radius * 0.1f))
    drawCircle(color = color, radius = radius * 0.95f, center = center)
    drawCircle(color = color, radius = radius * 0.70f, center = Offset(center.x + radius * 0.68f, center.y + radius * 0.12f))
}

@Composable
private fun ParticleBurstOverlay(
    event: HabitatEffectEvent,
    modifier: Modifier = Modifier
) {
    val progress = remember(event.id) { Animatable(0f) }
    LaunchedEffect(event.id) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 950, easing = FastOutSlowInEasing)
        )
    }

    val p = progress.value
    if (p >= 0.99f) return

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val count = 8
        for (i in 0 until count) {
            val angle = (-155f + (i * (130f / (count - 1)))) * (PI / 180.0)
            val dist = (46f + (i % 3) * 26f) * p
            val dx = (cos(angle) * dist * 1.4f).roundToInt()
            val dy = (sin(angle) * dist * 1.4f - 18f * p).roundToInt()
            val alpha = (1f - p * p).coerceIn(0f, 1f)
            val emoji = event.effect.emojis[i % event.effect.emojis.size]
            Text(
                text = emoji,
                fontSize = (18 + (i % 2) * 5).sp,
                modifier = Modifier
                    .offset { IntOffset(dx, dy) }
                    .alpha(alpha)
            )
        }
    }
}
