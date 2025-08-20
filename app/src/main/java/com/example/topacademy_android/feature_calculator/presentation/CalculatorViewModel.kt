package com.example.topacademy_android.feature_calculator.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topacademy_android.feature_calculator.data.CalculatorRepositoryImpl
import com.example.topacademy_android.feature_calculator.domain.CalculateExpressionUseCase

class CalculatorViewModel : ViewModel() {
    private val repository = CalculatorRepositoryImpl()
    private val calculateUseCase = CalculateExpressionUseCase(repository)

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun onCalculate(expression: String) {
        _result.value = calculateUseCase.execute(expression)
    }
}
