package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SolarPanel(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color(0xFF140F0A), shape = RoundedCornerShape(3.dp))
            .border(1.dp, Color(0xFF3D2C1C), shape = RoundedCornerShape(3.dp))
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(18.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF4D3823),
                                Color(0xFF261B10)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun RetroCalculatorScreen(
    state: CalculatorState = rememberCalculatorState()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121316))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Calculator Main Body Casing
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .shadow(16.dp, RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2C3038),
                            Color(0xFF20232A),
                            Color(0xFF17191E)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4F5563),
                            Color(0xFF121316)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            // Header: Brand & Solar Panel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RETRO-80",
                        color = Color(0xFFD4AF37),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "ELECTRONIC CALCULATOR",
                        color = Color(0xFF9E9E9E),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                SolarPanel(modifier = Modifier.width(96.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Display Housing Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0C0E10), shape = RoundedCornerShape(8.dp))
                    .border(2.dp, Color(0xFF040506), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Column {
                    // Operator status indicator inside screen
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (state.isError) "ERROR" else "DEG",
                            color = if (state.isError) Color(0xFFFF3333) else Color(0xFF09380E),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = state.currentOperator ?: "",
                            color = Color(0xFF00FF41),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Green 7-Segment Phosphor Display
                    SevenSegmentDisplay(
                        text = state.display,
                        modifier = Modifier.fillMaxWidth(),
                        maxDigits = 10,
                        activeColor = Color(0xFF00FF41),
                        inactiveColor = Color(0xFF06290A),
                        backgroundColor = Color(0xFF021204)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Keypad Grid with 3D Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Row 1: Function Keys & Divide
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Retro3DButton(
                        text = "AC",
                        onClick = { state.onAllClear() },
                        type = RetroButtonType.FUNCTION,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "C",
                        onClick = { state.onClearEntry() },
                        type = RetroButtonType.FUNCTION,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "±",
                        onClick = { state.onToggleSign() },
                        type = RetroButtonType.FUNCTION,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "÷",
                        onClick = { state.onOperator("÷") },
                        type = RetroButtonType.OPERATOR,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: 7, 8, 9, ×
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Retro3DButton(
                        text = "7",
                        onClick = { state.onDigit("7") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "8",
                        onClick = { state.onDigit("8") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "9",
                        onClick = { state.onDigit("9") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "×",
                        onClick = { state.onOperator("×") },
                        type = RetroButtonType.OPERATOR,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: 4, 5, 6, -
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Retro3DButton(
                        text = "4",
                        onClick = { state.onDigit("4") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "5",
                        onClick = { state.onDigit("5") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "6",
                        onClick = { state.onDigit("6") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "-",
                        onClick = { state.onOperator("-") },
                        type = RetroButtonType.OPERATOR,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 4: 1, 2, 3, +
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Retro3DButton(
                        text = "1",
                        onClick = { state.onDigit("1") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "2",
                        onClick = { state.onDigit("2") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "3",
                        onClick = { state.onDigit("3") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "+",
                        onClick = { state.onOperator("+") },
                        type = RetroButtonType.OPERATOR,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 5: 0, ., %, =
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Retro3DButton(
                        text = "0",
                        onClick = { state.onDigit("0") },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = ".",
                        onClick = { state.onDecimal() },
                        type = RetroButtonType.NUMBER,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "%",
                        onClick = { state.onPercent() },
                        type = RetroButtonType.FUNCTION,
                        modifier = Modifier.weight(1f)
                    )
                    Retro3DButton(
                        text = "=",
                        onClick = { state.onEquals() },
                        type = RetroButtonType.EQUALS,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
