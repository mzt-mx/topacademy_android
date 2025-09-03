package com.example.topacademy_android.feature_calculator.domain

import com.example.topacademy_android.feature_calculator.data.CalculatorParser

class CalculateExpressionUseCase(private val parser: CalculatorParser) {
    fun execute(expression: String): Double {
        return parser.eval(expression)
    }
}
