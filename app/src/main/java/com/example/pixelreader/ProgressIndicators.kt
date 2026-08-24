package com.example.pixelreader

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WavyLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "phase"
    )

    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()
        val waveLength = 20.dp.toPx()
        val amplitude = (size.height - strokeWidth) / 2f
        val progressWidth = size.width * progress

        // 1. тонкая прямая линия
        drawLine(
            color = trackColor,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 2. волнистый прогресс
        if (progressWidth > 0) {
            val path = Path()
            path.moveTo(0f, size.height / 2)

            for (x in 0..progressWidth.toInt()) {
                val y = size.height / 2 + sin((x / waveLength) * 2 * Math.PI + phase).toFloat() * amplitude
                path.lineTo(x.toFloat(), y)
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

@Composable
fun WavyCircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val infiniteTransition = rememberInfiniteTransition(label = "circular_wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "circular_phase"
    )

    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()
        val amplitude = 4.dp.toPx()

        val radius = size.minDimension / 2 - strokeWidth - amplitude
        val center = Offset(size.width / 2, size.height / 2)
        val waves = 8

        // 1. тонкий ровный круг
        drawCircle(
            color = trackColor,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        // 2. волнистый прогресс по кругу
        if (progress > 0) {
            val path = Path()
            val targetAngle = progress * 360f

            for (angle in 0..targetAngle.toInt()) {
                // старт сверху
                val rad = Math.toRadians(angle.toDouble() - 90.0)

                // Синусоида поверх радиуса
                val currentRadius = radius + sin(Math.toRadians(angle.toDouble() * waves) + phase).toFloat() * amplitude

                val x = center.x + currentRadius * cos(rad).toFloat()
                val y = center.y + currentRadius * sin(rad).toFloat()

                if (angle == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}