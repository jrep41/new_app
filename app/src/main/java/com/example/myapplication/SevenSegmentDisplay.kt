package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

data class SevenSegmentChar(
    val char: Char,
    val hasDecimal: Boolean = false
)

private fun getActiveSegments(c: Char): BooleanArray {
    // Segments: [A, B, C, D, E, F, G]
    return when (c.uppercaseChar()) {
        '0' -> booleanArrayOf(true, true, true, true, true, true, false)
        '1' -> booleanArrayOf(false, true, true, false, false, false, false)
        '2' -> booleanArrayOf(true, true, false, true, true, false, true)
        '3' -> booleanArrayOf(true, true, true, true, false, false, true)
        '4' -> booleanArrayOf(false, true, true, false, false, true, true)
        '5' -> booleanArrayOf(true, false, true, true, false, true, true)
        '6' -> booleanArrayOf(true, false, true, true, true, true, true)
        '7' -> booleanArrayOf(true, true, true, false, false, false, false)
        '8' -> booleanArrayOf(true, true, true, true, true, true, true)
        '9' -> booleanArrayOf(true, true, true, true, false, true, true)
        '-' -> booleanArrayOf(false, false, false, false, false, false, true)
        'E' -> booleanArrayOf(true, false, false, true, true, true, true)
        'R' -> booleanArrayOf(false, false, false, false, true, false, true)
        'O' -> booleanArrayOf(false, false, true, true, true, false, true)
        'C' -> booleanArrayOf(true, false, false, true, true, true, false)
        'L' -> booleanArrayOf(false, false, false, true, true, true, false)
        else -> booleanArrayOf(false, false, false, false, false, false, false)
    }
}

fun parseToSevenSegmentChars(text: String, maxDigits: Int = 10): List<SevenSegmentChar> {
    val result = mutableListOf<SevenSegmentChar>()
    var i = 0
    while (i < text.length && result.size < maxDigits) {
        val c = text[i]
        if (c == '.') {
            if (result.isNotEmpty()) {
                val lastIdx = result.size - 1
                result[lastIdx] = result[lastIdx].copy(hasDecimal = true)
            } else {
                result.add(SevenSegmentChar('0', hasDecimal = true))
            }
        } else {
            val nextIsDot = (i + 1 < text.length && text[i + 1] == '.')
            result.add(SevenSegmentChar(c, hasDecimal = nextIsDot))
            if (nextIsDot) {
                i++
            }
        }
        i++
    }
    val padding = List((maxDigits - result.size).coerceAtLeast(0)) { SevenSegmentChar(' ') }
    return padding + result
}

@Composable
fun SevenSegmentDigit(
    segmentChar: SevenSegmentChar,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF33FF33),
    inactiveColor: Color = Color(0xFF09290B)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val t = w * 0.15f
        val gap = t * 0.15f
        val active = getActiveSegments(segmentChar.char)

        // Segment A (Top)
        val pathA = Path().apply {
            moveTo(gap + t / 2, gap)
            lineTo(w - gap - t / 2, gap)
            lineTo(w - gap - t, gap + t)
            lineTo(gap + t, gap + t)
            close()
        }
        drawPath(pathA, color = if (active[0]) activeColor else inactiveColor)

        // Segment B (Top Right)
        val pathB = Path().apply {
            moveTo(w - gap, gap + t / 2)
            lineTo(w - gap, h / 2 - gap / 2)
            lineTo(w - gap - t, h / 2 - gap / 2 - t / 2)
            lineTo(w - gap - t, gap + t * 1.2f)
            close()
        }
        drawPath(pathB, color = if (active[1]) activeColor else inactiveColor)

        // Segment C (Bottom Right)
        val pathC = Path().apply {
            moveTo(w - gap, h / 2 + gap / 2)
            lineTo(w - gap, h - gap - t / 2)
            lineTo(w - gap - t, h - gap - t * 1.2f)
            lineTo(w - gap - t, h / 2 + gap / 2 + t / 2)
            close()
        }
        drawPath(pathC, color = if (active[2]) activeColor else inactiveColor)

        // Segment D (Bottom)
        val pathD = Path().apply {
            moveTo(gap + t, h - gap - t)
            lineTo(w - gap - t, h - gap - t)
            lineTo(w - gap - t / 2, h - gap)
            lineTo(gap + t / 2, h - gap)
            close()
        }
        drawPath(pathD, color = if (active[3]) activeColor else inactiveColor)

        // Segment E (Bottom Left)
        val pathE = Path().apply {
            moveTo(gap, h / 2 + gap / 2)
            lineTo(gap + t, h / 2 + gap / 2 + t / 2)
            lineTo(gap + t, h - gap - t * 1.2f)
            lineTo(gap, h - gap - t / 2)
            close()
        }
        drawPath(pathE, color = if (active[4]) activeColor else inactiveColor)

        // Segment F (Top Left)
        val pathF = Path().apply {
            moveTo(gap, gap + t / 2)
            lineTo(gap + t, gap + t * 1.2f)
            lineTo(gap + t, h / 2 - gap / 2 - t / 2)
            lineTo(gap, h / 2 - gap / 2)
            close()
        }
        drawPath(pathF, color = if (active[5]) activeColor else inactiveColor)

        // Segment G (Middle)
        val pathG = Path().apply {
            moveTo(gap + t, h / 2)
            lineTo(gap + t * 1.4f, h / 2 - t / 2)
            lineTo(w - gap - t * 1.4f, h / 2 - t / 2)
            lineTo(w - gap - t, h / 2)
            lineTo(w - gap - t * 1.4f, h / 2 + t / 2)
            lineTo(gap + t * 1.4f, h / 2 + t / 2)
            close()
        }
        drawPath(pathG, color = if (active[6]) activeColor else inactiveColor)

        // Decimal Point (DP)
        val dpRadius = t * 0.4f
        val dpCenter = Offset(w - dpRadius, h - dpRadius)
        drawCircle(
            color = if (segmentChar.hasDecimal) activeColor else inactiveColor,
            radius = dpRadius,
            center = dpCenter
        )
    }
}

@Composable
fun SevenSegmentDisplay(
    text: String,
    modifier: Modifier = Modifier,
    maxDigits: Int = 10,
    activeColor: Color = Color(0xFF00FF41),
    inactiveColor: Color = Color(0xFF072B0B),
    backgroundColor: Color = Color(0xFF021204)
) {
    val digits = parseToSevenSegmentChars(text, maxDigits)

    Box(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(6.dp))
            .border(2.dp, Color(0xFF1B3D1E), shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            digits.forEach { digit ->
                SevenSegmentDigit(
                    segmentChar = digit,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.5f)
                        .padding(horizontal = 1.dp)
                )
            }
        }

        // CRT Scanline texture overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scanlineColor = Color.Black.copy(alpha = 0.12f)
            var y = 0f
            val lineSpacing = 3.dp.toPx()
            while (y < size.height) {
                drawLine(
                    color = scanlineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
                y += lineSpacing
            }
        }

        // Screen glass glare reflection
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.02f),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
            )
        }
    }
}
