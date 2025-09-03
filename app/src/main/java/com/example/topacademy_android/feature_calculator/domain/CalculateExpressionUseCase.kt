package com.example.topacademy_android.feature_calculator.domain

import com.example.topacademy_android.feature_calculator.data.CalculatorParser
import java.util.Locale

class CalculateExpressionUseCase(private val parser: CalculatorParser) {
    fun execute(expression: String): String {
        return try {
            val result = parser.eval(expression)
            if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                String.format(Locale.US, "%.2f", result)
            }
        } catch (_: Exception) {
            "Ошибка"
        }
    }
}
