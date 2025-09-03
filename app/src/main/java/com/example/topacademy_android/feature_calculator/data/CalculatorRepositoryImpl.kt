package com.example.topacademy_android.feature_calculator.data

import com.example.topacademy_android.feature_calculator.data.CalculatorParser
import com.example.topacademy_android.feature_calculator.domain.CalculatorRepository

class CalculatorRepositoryImpl(private val parser: CalculatorParser) : CalculatorRepository {
    override fun calculate(expression: String): String {
        return try {
            val result = parser.eval(expression)
            if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                String.format("%.2f", result)
            }
        } catch (_: Exception) {
            "Ошибка"
        }
    }
}
