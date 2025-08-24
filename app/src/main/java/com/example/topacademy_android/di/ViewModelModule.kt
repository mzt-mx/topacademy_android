package com.example.topacademy_android.di

import com.example.topacademy_android.feature_calculator.presentation.CalculatorViewModel
import com.example.topacademy_android.feature_forecast.presentation.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CalculatorViewModel(get()) }
    viewModel { WeatherViewModel(get()) }
}
