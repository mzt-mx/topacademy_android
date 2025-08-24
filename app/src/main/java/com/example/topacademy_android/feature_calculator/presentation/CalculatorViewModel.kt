package com.example.topacademy_android.feature_calculator.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topacademy_android.feature_calculator.domain.CalculateExpressionUseCase

class CalculatorViewModel(private val calculateUseCase: CalculateExpressionUseCase) : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun onCalculate(expression: String) {
        _result.value = calculateUseCase.execute(expression)
    }
}
