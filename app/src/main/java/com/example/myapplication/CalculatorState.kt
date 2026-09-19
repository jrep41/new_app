package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.Locale
import kotlin.math.abs

class CalculatorState {
    var display by mutableStateOf("0")
        private set
    var firstOperand by mutableStateOf<Double?>(null)
        private set
    var currentOperator by mutableStateOf<String?>(null)
        private set
    var waitingForNextOperand by mutableStateOf(false)
        private set
    var isError by mutableStateOf(false)
        private set

    fun onDigit(digit: String) {
        if (isError) {
            onAllClear()
        }
        if (waitingForNextOperand) {
            display = digit
            waitingForNextOperand = false
        } else {
            if (display == "0") {
                display = digit
            } else if (display.replace("-", "").replace(".", "").length < 9) {
                display += digit
            }
        }
    }

    fun onDecimal() {
        if (isError) {
            onAllClear()
        }
        if (waitingForNextOperand) {
            display = "0."
            waitingForNextOperand = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    fun onOperator(op: String) {
        if (isError) return
        val valDouble = display.toDoubleOrNull() ?: return

        if (firstOperand != null && currentOperator != null && !waitingForNextOperand) {
            val res = calculate(firstOperand!!, valDouble, currentOperator!!)
            if (res == null) {
                setError()
                return
            }
            display = formatResult(res)
            firstOperand = res
        } else {
            firstOperand = valDouble
        }
        currentOperator = op
        waitingForNextOperand = true
    }

    fun onEquals() {
        if (isError) return
        val op = currentOperator ?: return
        val first = firstOperand ?: return
        val second = display.toDoubleOrNull() ?: return

        val res = calculate(first, second, op)
        if (res == null) {
            setError()
        } else {
            display = formatResult(res)
            firstOperand = null
            currentOperator = null
            waitingForNextOperand = true
        }
    }

    fun onClearEntry() {
        display = "0"
        if (isError) {
            onAllClear()
        }
    }

    fun onAllClear() {
        display = "0"
        firstOperand = null
        currentOperator = null
        waitingForNextOperand = false
        isError = false
    }

    fun onToggleSign() {
        if (isError || display == "0") return
        display = if (display.startsWith("-")) {
            display.substring(1)
        } else {
            "-$display"
        }
    }

    fun onPercent() {
        if (isError) return
        val valDouble = display.toDoubleOrNull() ?: return
        val res = valDouble / 100.0
        display = formatResult(res)
    }

    private fun setError() {
        isError = true
        display = "Error"
        firstOperand = null
        currentOperator = null
        waitingForNextOperand = true
    }

    private fun calculate(a: Double, b: Double, op: String): Double? {
        return when (op) {
            "+" -> a + b
            "-" -> a - b
            "×", "*" -> a * b
            "÷", "/" -> if (b == 0.0) null else a / b
            else -> null
        }
    }

    private fun formatResult(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        val absVal = abs(value)
        if (absVal >= 1e10) return "Error"

        if (value % 1.0 == 0.0 && absVal < 1e9) {
            return value.toLong().toString()
        }

        var str = String.format(Locale.US, "%.6f", value).trimEnd('0').trimEnd('.')
        if (str.length > 10) {
            str = String.format(Locale.US, "%.4f", value).trimEnd('0').trimEnd('.')
        }
        if (str.length > 10) {
            str = str.take(10)
        }
        return str
    }
}

@Composable
fun rememberCalculatorState(): CalculatorState {
    return remember { CalculatorState() }
}
