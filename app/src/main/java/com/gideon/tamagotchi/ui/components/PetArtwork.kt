package com.gideon.tamagotchi.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gideon.tamagotchi.R
import com.gideon.tamagotchi.domain.model.Pet
import com.gideon.tamagotchi.domain.model.PetStage
import com.gideon.tamagotchi.domain.model.PetType
import com.gideon.tamagotchi.ui.theme.GideonColors
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class PetMood(val messageRes: Int) {
    HAPPY(R.string.mood_happy),
    NEUTRAL(R.string.mood_neutral),
    HUNGRY(R.string.mood_hungry),
    DIRTY(R.string.mood_dirty),
    TIRED(R.string.mood_tired),
    SICK(R.string.mood_sick),
    SLEEPING(R.string.mood_sleeping),
    DEAD(R.string.game_over_memorial)
}

fun computePetMood(pet: Pet): PetMood = when {
    !pet.isAlive -> PetMood.DEAD
    pet.isSleeping -> PetMood.SLEEPING
    pet.health < 35 -> PetMood.SICK
    pet.hunger < 35 -> PetMood.HUNGRY
    pet.hygiene < 35 -> PetMood.DIRTY
    pet.energy < 35 -> PetMood.TIRED
    pet.health >= 75 -> PetMood.HAPPY
    else -> PetMood.NEUTRAL
}

@Composable
fun PetCharacterCanvas(
    type: PetType,
    stage: PetStage,
    colorHex: Long,
    mood: PetMood,
    modifier: Modifier = Modifier,
    size: Dp = 172.dp,
    onTap: (() -> Unit)? = null
) {
    val infinite = rememberInfiniteTransition(label = "pet_anim")

    val breathProgress by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (mood) {
                    PetMood.SLEEPING -> 2200
                    PetMood.SICK, PetMood.TIRED -> 1700
                    PetMood.DEAD -> 2600
                    PetMood.HAPPY -> 750
                    else -> 1100
                },
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )

    val floatPhase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "float_phase"
    )

    // Organic periodic blinking
    var isBlinking by remember { mutableStateOf(false) }
    LaunchedEffect(mood) {
        if (mood == PetMood.SLEEPING || mood == PetMood.DEAD) {
            isBlinking = false
            return@LaunchedEffect
        }
        while (true) {
            delay(2800L)
            isBlinking = true
            delay(140L)
            isBlinking = false
        }
    }

    // Squish-bounce on tap
    var tapBounce by remember { mutableStateOf(false) }
    LaunchedEffect(tapBounce) {
        if (tapBounce) {
            delay(240L)
            tapBounce = false
        }
    }
    val tapScale by animateFloatAsState(
        targetValue = if (tapBounce) 1.14f else 1f,
        animationSpec = spring(dampingRatio = 0.42f, stiffness = 420f),
        label = "tap_scale"
    )

    val stageScale = when (stage) {
        PetStage.BABY -> 0.78f
        PetStage.CHILD -> 0.88f
        PetStage.TEEN -> 0.95f
        PetStage.ADULT -> 1.02f
    }

    val baseColor = remember(colorHex, mood) {
        val raw = Color(colorHex)
        when (mood) {
            PetMood.DEAD -> Color(0xFFB4B8C4)
            PetMood.SICK -> lerp(raw, Color(0xFF9FB89D), 0.35f)
            else -> raw
        }
    }

    val clickableModifier = if (onTap != null && mood != PetMood.DEAD) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            tapBounce = true
            onTap()
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(size)
            .then(clickableModifier),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h * 0.56f
            val radius = (minOf(w, h) * 0.34f) * stageScale

            val bobY = when (mood) {
                PetMood.DEAD -> sin(floatPhase) * (h * 0.035f)
                PetMood.SLEEPING -> breathProgress * (h * 0.012f)
                PetMood.HAPPY -> -breathProgress * (h * 0.055f)
                PetMood.SICK -> sin(floatPhase * 4f) * (h * 0.008f)
                else -> -breathProgress * (h * 0.025f)
            }
            val shiverX = if (mood == PetMood.SICK) sin(floatPhase * 8f) * (w * 0.012f) else 0f

            val squashX = when (mood) {
                PetMood.DEAD -> 1f
                PetMood.SLEEPING -> 1f + breathProgress * 0.04f
                PetMood.HAPPY -> 1f - breathProgress * 0.045f
                else -> 1f + (0.5f - breathProgress) * 0.04f
            } * tapScale
            val squashY = when (mood) {
                PetMood.DEAD -> 1f
                PetMood.SLEEPING -> 1f - breathProgress * 0.035f
                PetMood.HAPPY -> 1f + breathProgress * 0.06f
                else -> 1f - (0.5f - breathProgress) * 0.04f
            } * (2f - tapScale).coerceIn(0.88f, 1.15f)

            // Soft ground shadow
            val shadowScale = (1f - (-bobY / (h * 0.25f))).coerceIn(0.65f, 1.05f)
            drawOval(
                color = Color.Black.copy(alpha = if (mood == PetMood.DEAD) 0.08f else 0.16f),
                topLeft = Offset(
                    x = cx - radius * 0.85f * shadowScale,
                    y = h * 0.85f
                ),
                size = Size(
                    width = radius * 1.7f * shadowScale,
                    height = radius * 0.34f * shadowScale
                )
            )

            // Adult golden aura behind pet
            if (stage == PetStage.ADULT && mood != PetMood.DEAD && mood != PetMood.SICK) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GideonColors.GoldCrown.copy(alpha = 0.28f + breathProgress * 0.14f),
                            Color.Transparent
                        ),
                        center = Offset(cx, cy + bobY),
                        radius = radius * 1.55f
                    ),
                    radius = radius * 1.55f,
                    center = Offset(cx, cy + bobY)
                )
            }

            // Main pet body + face + stage accessories
            translate(left = shiverX, top = bobY) {
                scale(scaleX = squashX, scaleY = squashY, pivot = Offset(cx, cy + radius * 0.4f)) {
                    when (type) {
                        PetType.BLOB -> drawBlobBody(cx, cy, radius, baseColor, mood)
                        PetType.COOKIE -> drawCookieBody(cx, cy, radius, baseColor, mood)
                        PetType.STAR -> drawStarBody(cx, cy, radius, baseColor, mood)
                    }

                    drawPetFace(
                        cx = cx,
                        cy = cy + (if (type == PetType.STAR) radius * 0.04f else 0f),
                        radius = radius,
                        mood = mood,
                        isBlinking = isBlinking
                    )

                    drawStageAccessory(
                        cx = cx,
                        cy = cy,
                        radius = radius,
                        stage = stage,
                        mood = mood,
                        type = type,
                        floatPhase = floatPhase
                    )
                }
            }

            // Mood atmospheric overlays (outside squash transform)
            when (mood) {
                PetMood.SLEEPING -> drawSleepZzz(cx, cy - radius * 1.05f, radius, floatPhase)
                PetMood.DIRTY -> drawDirtyEffects(cx, cy, radius, floatPhase)
                PetMood.DEAD -> drawAngelHalo(cx, cy - radius * 1.25f + bobY, radius)
                PetMood.TIRED -> drawSweatDrop(cx + radius * 0.78f, cy - radius * 0.25f + bobY, radius)
                else -> Unit
            }
        }
    }
}

private fun DrawScope.drawBlobBody(
    cx: Float,
    cy: Float,
    r: Float,
    color: Color,
    mood: PetMood
) {
    val highlight = lerp(color, Color.White, 0.35f)
    val shadow = lerp(color, Color.Black, 0.22f)
    val outlineColor = lerp(color, Color.Black, 0.45f)

    // Squishy rounded jelly drop path
    val bodyPath = Path().apply {
        moveTo(cx, cy - r * 1.02f)
        cubicTo(
            cx + r * 0.75f, cy - r * 1.02f,
            cx + r * 1.12f, cy - r * 0.25f,
            cx + r * 1.05f, cy + r * 0.42f
        )
        cubicTo(
            cx + r * 0.98f, cy + r * 0.92f,
            cx + r * 0.52f, cy + r * 0.98f,
            cx, cy + r * 0.98f
        )
        cubicTo(
            cx - r * 0.52f, cy + r * 0.98f,
            cx - r * 0.98f, cy + r * 0.92f,
            cx - r * 1.05f, cy + r * 0.42f
        )
        cubicTo(
            cx - r * 1.12f, cy - r * 0.25f,
            cx - r * 0.75f, cy - r * 1.02f,
            cx, cy - r * 1.02f
        )
        close()
    }

    // Little stubby ears / nubs on top sides
    val earOffsetX = r * 0.58f
    val earOffsetY = -r * 0.74f
    drawCircle(
        color = color,
        radius = r * 0.24f,
        center = Offset(cx - earOffsetX, cy + earOffsetY)
    )
    drawCircle(
        color = outlineColor,
        radius = r * 0.24f,
        center = Offset(cx - earOffsetX, cy + earOffsetY),
        style = Stroke(width = r * 0.06f)
    )
    drawCircle(
        color = color,
        radius = r * 0.24f,
        center = Offset(cx + earOffsetX, cy + earOffsetY)
    )
    drawCircle(
        color = outlineColor,
        radius = r * 0.24f,
        center = Offset(cx + earOffsetX, cy + earOffsetY),
        style = Stroke(width = r * 0.06f)
    )

    // Main jelly gradient fill
    drawPath(
        path = bodyPath,
        brush = Brush.radialGradient(
            colors = listOf(highlight, color, shadow),
            center = Offset(cx - r * 0.25f, cy - r * 0.35f),
            radius = r * 1.55f
        ),
        alpha = if (mood == PetMood.DEAD) 0.78f else 1f
    )
    drawPath(
        path = bodyPath,
        color = outlineColor,
        style = Stroke(width = r * 0.065f, join = StrokeJoin.Round)
    )

    // Glossy specular shine
    rotate(degrees = -28f, pivot = Offset(cx - r * 0.42f, cy - r * 0.46f)) {
        drawOval(
            color = Color.White.copy(alpha = 0.55f),
            topLeft = Offset(cx - r * 0.62f, cy - r * 0.58f),
            size = Size(r * 0.40f, r * 0.20f)
        )
    }
    drawCircle(
        color = Color.White.copy(alpha = 0.45f),
        radius = r * 0.06f,
        center = Offset(cx - r * 0.16f, cy - r * 0.64f)
    )
}

private fun DrawScope.drawCookieBody(
    cx: Float,
    cy: Float,
    r: Float,
    color: Color,
    mood: PetMood
) {
    val highlight = lerp(color, Color(0xFFFFF3DC), 0.32f)
    val crust = lerp(color, Color(0xFF4A2511), 0.32f)
    val outlineColor = lerp(color, Color(0xFF2B1408), 0.52f)
    val chipColor = lerp(color, Color(0xFF2E1508), 0.68f)

    // Scalloped bumpy biscuit edge (12 lobes)
    val scallops = 12
    val cookiePath = Path()
    for (i in 0 until scallops * 4) {
        val angle = (i.toFloat() / (scallops * 4)) * 2f * PI.toFloat()
        val bump = 1f + 0.065f * sin(angle * scallops)
        val px = cx + cos(angle) * r * 1.03f * bump
        val py = cy + sin(angle) * r * 1.00f * bump
        if (i == 0) cookiePath.moveTo(px, py) else cookiePath.lineTo(px, py)
    }
    cookiePath.close()

    drawPath(
        path = cookiePath,
        brush = Brush.radialGradient(
            colors = listOf(highlight, color, crust),
            center = Offset(cx - r * 0.2f, cy - r * 0.25f),
            radius = r * 1.3f
        ),
        alpha = if (mood == PetMood.DEAD) 0.78f else 1f
    )
    drawPath(
        path = cookiePath,
        color = outlineColor,
        style = Stroke(width = r * 0.065f, join = StrokeJoin.Round)
    )

    // Chocolate chips placed around the face
    val chips = listOf(
        Triple(-0.56f, -0.48f, 0.12f),
        Triple(0.54f, -0.52f, 0.11f),
        Triple(-0.66f, 0.28f, 0.10f),
        Triple(0.64f, 0.32f, 0.12f),
        Triple(-0.18f, 0.66f, 0.10f),
        Triple(0.28f, 0.62f, 0.09f),
        Triple(0.05f, -0.70f, 0.09f)
    )
    chips.forEach { (ox, oy, sr) ->
        drawCircle(
            color = chipColor,
            radius = r * sr,
            center = Offset(cx + r * ox, cy + r * oy)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.25f),
            radius = r * sr * 0.32f,
            center = Offset(cx + r * ox - r * sr * 0.28f, cy + r * oy - r * sr * 0.28f)
        )
    }
}

private fun DrawScope.drawStarBody(
    cx: Float,
    cy: Float,
    r: Float,
    color: Color,
    mood: PetMood
) {
    val highlight = lerp(color, Color.White, 0.42f)
    val shadow = lerp(color, Color.Black, 0.20f)
    val outlineColor = lerp(color, Color.Black, 0.46f)

    // Plump, rounded 5-point plush star
    val starPath = Path()
    val points = 5
    val outerR = r * 1.18f
    val innerR = r * 0.62f
    val totalSteps = 100
    for (i in 0..totalSteps) {
        val t = (i.toFloat() / totalSteps) * 2f * PI.toFloat() - (PI / 2f).toFloat()
        val wave = (cos(points * (t + (PI / 2f).toFloat())) + 1f) / 2f
        // Smooth power curve for chubby star tips
        val smoothWave = wave * wave * (3f - 2f * wave)
        val curR = innerR + (outerR - innerR) * smoothWave
        val px = cx + cos(t) * curR
        val py = cy + sin(t) * curR
        if (i == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)
    }
    starPath.close()

    drawPath(
        path = starPath,
        brush = Brush.radialGradient(
            colors = listOf(highlight, color, shadow),
            center = Offset(cx - r * 0.15f, cy - r * 0.25f),
            radius = r * 1.35f
        ),
        alpha = if (mood == PetMood.DEAD) 0.78f else 1f
    )
    drawPath(
        path = starPath,
        color = outlineColor,
        style = Stroke(width = r * 0.065f, join = StrokeJoin.Round)
    )

    // Sparkly top sheen
    drawCircle(
        color = Color.White.copy(alpha = 0.55f),
        radius = r * 0.11f,
        center = Offset(cx - r * 0.18f, cy - r * 0.48f)
    )
}

private fun DrawScope.drawPetFace(
    cx: Float,
    cy: Float,
    radius: Float,
    mood: PetMood,
    isBlinking: Boolean
) {
    val eyeX = radius * 0.34f
    val eyeY = cy - radius * 0.06f
    val eyeR = radius * 0.135f
    val darkInk = Color(0xFF231820)

    // Rosy cheeks (alive and not dead)
    if (mood != PetMood.DEAD) {
        val cheekColor = if (mood == PetMood.SICK) {
            Color(0xFF7CB342).copy(alpha = 0.35f)
        } else {
            Color(0xFFFF6B9D).copy(alpha = 0.45f)
        }
        drawOval(
            color = cheekColor,
            topLeft = Offset(cx - radius * 0.62f, cy + radius * 0.08f),
            size = Size(radius * 0.28f, radius * 0.16f)
        )
        drawOval(
            color = cheekColor,
            topLeft = Offset(cx + radius * 0.34f, cy + radius * 0.08f),
            size = Size(radius * 0.28f, radius * 0.16f)
        )
    }

    // Eyes by mood
    val eyeStroke = Stroke(width = radius * 0.075f, cap = StrokeCap.Round)
    when {
        mood == PetMood.DEAD -> {
            // X X eyes
            val d = eyeR * 0.85f
            listOf(cx - eyeX, cx + eyeX).forEach { ex ->
                drawLine(darkInk, Offset(ex - d, eyeY - d), Offset(ex + d, eyeY + d), strokeWidth = radius * 0.075f, cap = StrokeCap.Round)
                drawLine(darkInk, Offset(ex + d, eyeY - d), Offset(ex - d, eyeY + d), strokeWidth = radius * 0.075f, cap = StrokeCap.Round)
            }
        }

        mood == PetMood.SLEEPING -> {
            // Peaceful downward curves u u
            listOf(cx - eyeX, cx + eyeX).forEach { ex ->
                drawArc(
                    color = darkInk,
                    startAngle = 15f,
                    sweepAngle = 150f,
                    useCenter = false,
                    topLeft = Offset(ex - eyeR, eyeY - eyeR * 0.6f),
                    size = Size(eyeR * 2f, eyeR * 1.4f),
                    style = eyeStroke
                )
            }
        }

        isBlinking -> {
            // Blinking horizontal lines
            listOf(cx - eyeX, cx + eyeX).forEach { ex ->
                drawLine(
                    color = darkInk,
                    start = Offset(ex - eyeR, eyeY),
                    end = Offset(ex + eyeR, eyeY),
                    strokeWidth = radius * 0.075f,
                    cap = StrokeCap.Round
                )
            }
        }

        mood == PetMood.HAPPY -> {
            // Joyful upturned ^ ^ arcs
            listOf(cx - eyeX, cx + eyeX).forEach { ex ->
                drawArc(
                    color = darkInk,
                    startAngle = 200f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(ex - eyeR * 1.05f, eyeY - eyeR * 0.7f),
                    size = Size(eyeR * 2.1f, eyeR * 1.6f),
                    style = eyeStroke
                )
            }
        }

        mood == PetMood.SICK -> {
            // Dizzy > < eyes
            val d = eyeR * 0.8f
            val leftPath = Path().apply {
                moveTo(cx - eyeX - d, eyeY - d * 0.7f)
                lineTo(cx - eyeX + d * 0.6f, eyeY)
                lineTo(cx - eyeX - d, eyeY + d * 0.7f)
            }
            val rightPath = Path().apply {
                moveTo(cx + eyeX + d, eyeY - d * 0.7f)
                lineTo(cx + eyeX - d * 0.6f, eyeY)
                lineTo(cx + eyeX + d, eyeY + d * 0.7f)
            }
            drawPath(leftPath, darkInk, style = eyeStroke)
            drawPath(rightPath, darkInk, style = eyeStroke)
        }

        else -> {
            // Glossy kawaii round eyes with double catchlights
            listOf(cx - eyeX, cx + eyeX).forEach { ex ->
                drawCircle(color = darkInk, radius = eyeR, center = Offset(ex, eyeY))
                drawCircle(
                    color = Color.White,
                    radius = eyeR * 0.38f,
                    center = Offset(ex - eyeR * 0.28f, eyeY - eyeR * 0.28f)
                )
                drawCircle(
                    color = Color.White,
                    radius = eyeR * 0.18f,
                    center = Offset(ex + eyeR * 0.28f, eyeY + eyeR * 0.24f)
                )
                // Droopy eyelids for TIRED
                if (mood == PetMood.TIRED) {
                    drawLine(
                        color = darkInk,
                        start = Offset(ex - eyeR * 1.15f, eyeY - eyeR * 0.45f),
                        end = Offset(ex + eyeR * 1.15f, eyeY - eyeR * 0.2f),
                        strokeWidth = radius * 0.08f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }

    // Mouth by mood
    val mouthY = cy + radius * 0.20f
    val mouthStroke = Stroke(width = radius * 0.068f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    when (mood) {
        PetMood.HAPPY -> {
            val smilePath = Path().apply {
                moveTo(cx - radius * 0.22f, mouthY - radius * 0.04f)
                quadraticTo(cx, mouthY + radius * 0.28f, cx + radius * 0.22f, mouthY - radius * 0.04f)
                close()
            }
            drawPath(smilePath, color = Color(0xFFFF6B8B))
            drawPath(smilePath, color = darkInk, style = mouthStroke)
        }

        PetMood.NEUTRAL -> {
            // Cute cat-like w smile
            val wPath = Path().apply {
                moveTo(cx - radius * 0.18f, mouthY)
                quadraticTo(cx - radius * 0.09f, mouthY + radius * 0.14f, cx, mouthY + radius * 0.02f)
                quadraticTo(cx + radius * 0.09f, mouthY + radius * 0.14f, cx + radius * 0.18f, mouthY)
            }
            drawPath(wPath, color = darkInk, style = mouthStroke)
        }

        PetMood.HUNGRY -> {
            // Open pleading mouth
            drawOval(
                color = Color(0xFFD94A64),
                topLeft = Offset(cx - radius * 0.14f, mouthY - radius * 0.02f),
                size = Size(radius * 0.28f, radius * 0.22f)
            )
            drawOval(
                color = darkInk,
                topLeft = Offset(cx - radius * 0.14f, mouthY - radius * 0.02f),
                size = Size(radius * 0.28f, radius * 0.22f),
                style = mouthStroke
            )
        }

        PetMood.SLEEPING -> {
            // Tiny peaceful 'o' breath mouth
            drawCircle(
                color = darkInk,
                radius = radius * 0.065f,
                center = Offset(cx, mouthY + radius * 0.04f),
                style = mouthStroke
            )
        }

        PetMood.SICK, PetMood.DIRTY, PetMood.TIRED -> {
            // Wavy worried frown
            val frownPath = Path().apply {
                moveTo(cx - radius * 0.18f, mouthY + radius * 0.10f)
                quadraticTo(cx, mouthY - radius * 0.08f, cx + radius * 0.18f, mouthY + radius * 0.10f)
            }
            drawPath(frownPath, color = darkInk, style = mouthStroke)
        }

        PetMood.DEAD -> {
            drawLine(
                color = darkInk,
                start = Offset(cx - radius * 0.14f, mouthY + radius * 0.06f),
                end = Offset(cx + radius * 0.14f, mouthY + radius * 0.06f),
                strokeWidth = radius * 0.065f,
                cap = StrokeCap.Round
            )
        }
    }

    // Forehead bandage when sick
    if (mood == PetMood.SICK) {
        rotate(degrees = -12f, pivot = Offset(cx + radius * 0.25f, cy - radius * 0.48f)) {
            drawRoundRect(
                color = Color(0xFFFFF3E0),
                topLeft = Offset(cx + radius * 0.02f, cy - radius * 0.56f),
                size = Size(radius * 0.46f, radius * 0.18f),
                cornerRadius = CornerRadius(radius * 0.06f)
            )
            drawRoundRect(
                color = Color(0xFFD7B899),
                topLeft = Offset(cx + radius * 0.02f, cy - radius * 0.56f),
                size = Size(radius * 0.46f, radius * 0.18f),
                cornerRadius = CornerRadius(radius * 0.06f),
                style = Stroke(width = radius * 0.03f)
            )
        }
    }
}

private fun DrawScope.drawStageAccessory(
    cx: Float,
    cy: Float,
    radius: Float,
    stage: PetStage,
    mood: PetMood,
    type: PetType,
    floatPhase: Float
) {
    if (mood == PetMood.DEAD) return
    val topY = cy - radius * (if (type == PetType.STAR) 1.12f else 1.00f)

    when (stage) {
        PetStage.BABY -> {
            // Cute little baby curl sprout on top
            val curl = Path().apply {
                moveTo(cx, topY + radius * 0.04f)
                cubicTo(
                    cx - radius * 0.15f, topY - radius * 0.28f,
                    cx + radius * 0.25f, topY - radius * 0.34f,
                    cx + radius * 0.12f, topY - radius * 0.14f
                )
            }
            drawPath(
                path = curl,
                color = Color(0xFF231820),
                style = Stroke(width = radius * 0.065f, cap = StrokeCap.Round)
            )
        }

        PetStage.CHILD -> {
            // Cute little twin green sprout leaves on top
            val stemTop = Offset(cx, topY - radius * 0.24f)
            drawLine(
                color = Color(0xFF2F9E44),
                start = Offset(cx, topY + radius * 0.04f),
                end = stemTop,
                strokeWidth = radius * 0.065f,
                cap = StrokeCap.Round
            )
            rotate(-28f, pivot = stemTop) {
                drawOval(
                    color = Color(0xFF51CF66),
                    topLeft = Offset(stemTop.x - radius * 0.28f, stemTop.y - radius * 0.11f),
                    size = Size(radius * 0.28f, radius * 0.15f)
                )
            }
            rotate(28f, pivot = stemTop) {
                drawOval(
                    color = Color(0xFF40C057),
                    topLeft = Offset(stemTop.x, stemTop.y - radius * 0.11f),
                    size = Size(radius * 0.28f, radius * 0.15f)
                )
            }
        }

        PetStage.TEEN -> {
            // Cute pastel bow / ribbon on top-right
            val bowX = cx + radius * 0.32f
            val bowY = topY + radius * 0.10f
            val bowColor = Color(0xFFFF5C8D)
            val bowOutline = Color(0xFF8C1D40)
            val leftWing = Path().apply {
                moveTo(bowX, bowY)
                lineTo(bowX - radius * 0.28f, bowY - radius * 0.16f)
                lineTo(bowX - radius * 0.24f, bowY + radius * 0.14f)
                close()
            }
            val rightWing = Path().apply {
                moveTo(bowX, bowY)
                lineTo(bowX + radius * 0.28f, bowY - radius * 0.14f)
                lineTo(bowX + radius * 0.24f, bowY + radius * 0.16f)
                close()
            }
            drawPath(leftWing, bowColor)
            drawPath(rightWing, bowColor)
            drawPath(leftWing, bowOutline, style = Stroke(width = radius * 0.04f, join = StrokeJoin.Round))
            drawPath(rightWing, bowOutline, style = Stroke(width = radius * 0.04f, join = StrokeJoin.Round))
            drawCircle(color = Color(0xFFFFD43B), radius = radius * 0.08f, center = Offset(bowX, bowY))
        }

        PetStage.ADULT -> {
            // Floating golden royal crown!
            val crownBob = sin(floatPhase * 1.5f) * radius * 0.05f
            val crownBaseY = topY - radius * 0.08f + crownBob
            val cw = radius * 0.44f
            val ch = radius * 0.34f
            val crownPath = Path().apply {
                moveTo(cx - cw * 0.85f, crownBaseY)
                lineTo(cx - cw, crownBaseY - ch * 0.85f)
                lineTo(cx - cw * 0.35f, crownBaseY - ch * 0.40f)
                lineTo(cx, crownBaseY - ch * 1.15f)
                lineTo(cx + cw * 0.35f, crownBaseY - ch * 0.40f)
                lineTo(cx + cw, crownBaseY - ch * 0.85f)
                lineTo(cx + cw * 0.85f, crownBaseY)
                close()
            }
            drawPath(crownPath, color = GideonColors.GoldCrown)
            drawPath(
                crownPath,
                color = Color(0xFFB4690E),
                style = Stroke(width = radius * 0.05f, join = StrokeJoin.Round)
            )
            // Tiny ruby jewel in center of crown
            drawCircle(
                color = Color(0xFFE0316E),
                radius = radius * 0.065f,
                center = Offset(cx, crownBaseY - ch * 0.32f)
            )
        }
    }
}

private fun DrawScope.drawSleepZzz(cx: Float, topY: Float, r: Float, phase: Float) {
    val norm = (phase / (2f * PI.toFloat()))
    for (i in 0..2) {
        val progress = (norm + i * 0.33f) % 1f
        val alpha = sin(progress * PI.toFloat()).coerceIn(0f, 1f) * 0.85f
        val x = cx + r * 0.65f + progress * r * 0.45f
        val y = topY + r * 0.35f - progress * r * 0.85f
        val zSize = r * (0.14f + progress * 0.14f)
        val zPath = Path().apply {
            moveTo(x - zSize / 2f, y - zSize / 2f)
            lineTo(x + zSize / 2f, y - zSize / 2f)
            lineTo(x - zSize / 2f, y + zSize / 2f)
            lineTo(x + zSize / 2f, y + zSize / 2f)
        }
        drawPath(
            path = zPath,
            color = Color(0xFFC4B5FD).copy(alpha = alpha),
            style = Stroke(width = r * 0.055f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

private fun DrawScope.drawDirtyEffects(cx: Float, cy: Float, r: Float, phase: Float) {
    // Wavy green stink lines rising on sides
    val stinkColor = Color(0xFF7CB342).copy(alpha = 0.65f)
    listOf(-0.95f, 0.95f).forEachIndexed { idx, side ->
        val baseX = cx + r * side
        val wave = sin(phase * 2f + idx * 1.5f) * r * 0.08f
        val stinkPath = Path().apply {
            moveTo(baseX, cy + r * 0.15f)
            cubicTo(
                baseX - r * 0.12f + wave, cy - r * 0.15f,
                baseX + r * 0.12f - wave, cy - r * 0.45f,
                baseX + wave * 0.5f, cy - r * 0.72f
            )
        }
        drawPath(
            path = stinkPath,
            color = stinkColor,
            style = Stroke(width = r * 0.055f, cap = StrokeCap.Round)
        )
    }
}

private fun DrawScope.drawAngelHalo(cx: Float, y: Float, r: Float) {
    drawOval(
        color = GideonColors.GoldCrown.copy(alpha = 0.9f),
        topLeft = Offset(cx - r * 0.52f, y - r * 0.13f),
        size = Size(r * 1.04f, r * 0.26f),
        style = Stroke(width = r * 0.08f)
    )
}

private fun DrawScope.drawSweatDrop(x: Float, y: Float, r: Float) {
    val drop = Path().apply {
        moveTo(x, y - r * 0.18f)
        quadraticTo(x + r * 0.16f, y + r * 0.08f, x, y + r * 0.16f)
        quadraticTo(x - r * 0.16f, y + r * 0.08f, x, y - r * 0.18f)
        close()
    }
    drawPath(drop, color = Color(0xFF38BDF8))
    drawCircle(
        color = Color.White.copy(alpha = 0.7f),
        radius = r * 0.035f,
        center = Offset(x - r * 0.03f, y + r * 0.02f)
    )
}
