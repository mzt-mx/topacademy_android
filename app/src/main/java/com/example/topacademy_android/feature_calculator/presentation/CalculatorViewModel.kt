package com.example.topacademy_android.feature_calculator.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topacademy_android.feature_calculator.domain.CalculateExpressionUseCase
import java.util.Locale

class CalculatorViewModel(private val calculateUseCase: CalculateExpressionUseCase) : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun onCalculate(expression: String) {
        try {
            val result = calculateUseCase.execute(expression)
            _result.value = if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                String.format(Locale.US, "%.2f", result)
            }
        } catch (e: Exception) {
            _result.value = "Ошибка"
        }
    }
}
