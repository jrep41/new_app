package com.example.myapplication

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class RetroButtonType {
    NUMBER,
    OPERATOR,
    FUNCTION,
    EQUALS
}

@Composable
fun Retro3DButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: RetroButtonType = RetroButtonType.NUMBER,
    customColor: Color? = null
) {
    var isPressed by remember { mutableStateOf(false) }

    val (faceColor, textColor, highlightColor, shadowColor) = when (type) {
        RetroButtonType.NUMBER -> QuadColors(
            face = customColor ?: Color(0xFF383C44),
            text = Color(0xFFF5F5F5),
            highlight = Color(0xFF5E6470),
            shadow = Color(0xFF1B1D22)
        )
        RetroButtonType.OPERATOR -> QuadColors(
            face = customColor ?: Color(0xFF2C4C5E),
            text = Color(0xFFFFD54F),
            highlight = Color(0xFF4A748C),
            shadow = Color(0xFF13242E)
        )
        RetroButtonType.FUNCTION -> QuadColors(
            face = customColor ?: Color(0xFF8C2D19),
            text = Color(0xFFFFFFFF),
            highlight = Color(0xFFB5402A),
            shadow = Color(0xFF42120A)
        )
        RetroButtonType.EQUALS -> QuadColors(
            face = customColor ?: Color(0xFF1E7A40),
            text = Color(0xFFFFFFFF),
            highlight = Color(0xFF2FA85C),
            shadow = Color(0xFF0C381B)
        )
    }

    val offsetY: Dp by animateDpAsState(
        targetValue = if (isPressed) 3.dp else 0.dp,
        label = "buttonOffset"
    )

    val shadowElevation = if (isPressed) 1.dp else 5.dp

    Box(
        modifier = modifier
            .offset(y = offsetY)
            .shadow(
                elevation = shadowElevation,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isPressed) {
                        listOf(shadowColor, faceColor)
                    } else {
                        listOf(highlightColor, faceColor, shadowColor)
                    }
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        val released = try {
                            tryAwaitRelease()
                        } catch (e: Exception) {
                            false
                        }
                        isPressed = false
                        if (released) {
                            onClick()
                        }
                    }
                )
            }
            .drawWithContent {
                drawContent()

                val w = size.width
                val h = size.height
                val bevel = 4.dp.toPx()

                val topHighlight = if (isPressed) Color.Black.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.35f)
                val bottomShadow = if (isPressed) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.55f)

                // Draw 3D Bevel Top-Left
                val pathTopLeft = Path().apply {
                    moveTo(0f, h)
                    lineTo(0f, 0f)
                    lineTo(w, 0f)
                    lineTo(w - bevel, bevel)
                    lineTo(bevel, bevel)
                    lineTo(bevel, h - bevel)
                    close()
                }
                drawPath(pathTopLeft, color = topHighlight)

                // Draw 3D Bevel Bottom-Right
                val pathBottomRight = Path().apply {
                    moveTo(w, 0f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    lineTo(bevel, h - bevel)
                    lineTo(w - bevel, h - bevel)
                    lineTo(w - bevel, bevel)
                    close()
                }
                drawPath(pathBottomRight, color = bottomShadow)

                // Tactile inner key dish reflection
                val dishMargin = 6.dp.toPx()
                drawRoundRect(
                    color = Color.White.copy(alpha = if (isPressed) 0.02f else 0.08f),
                    topLeft = Offset(dishMargin, dishMargin),
                    size = Size(w - 2 * dishMargin, (h - 2 * dishMargin) * 0.45f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

private data class QuadColors(
    val face: Color,
    val text: Color,
    val highlight: Color,
    val shadow: Color
)
