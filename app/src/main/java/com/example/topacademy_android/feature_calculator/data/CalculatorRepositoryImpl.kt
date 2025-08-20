package com.example.topacademy_android.feature_calculator.data

import com.example.topacademy_android.feature_calculator.domain.CalculatorRepository
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorRepositoryImpl : CalculatorRepository {
    override fun calculate(expression: String): String {
        return try {
            val result = ExpressionBuilder(expression).build().evaluate()
            if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
        } catch (e: Exception) {
            "Ошибка"
        }
    }
}
