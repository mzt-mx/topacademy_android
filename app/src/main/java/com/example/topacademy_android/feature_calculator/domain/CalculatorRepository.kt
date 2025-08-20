package com.example.topacademy_android.feature_calculator.domain

interface CalculatorRepository {
    fun calculate(expression: String): String
}
