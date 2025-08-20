package com.example.topacademy_android.feature_calculator.domain

class CalculateExpressionUseCase(private val repository: CalculatorRepository) {
    fun execute(expression: String): String {
        return repository.calculate(expression)
    }
}
