package com.example.topacademy_android.di

import com.example.topacademy_android.feature_calculator.data.CalculatorRepositoryImpl
import com.example.topacademy_android.feature_calculator.domain.CalculateExpressionUseCase
import com.example.topacademy_android.feature_calculator.domain.CalculatorRepository
import com.example.topacademy_android.feature_forecast.data.WeatherRepositoryImpl
import com.example.topacademy_android.feature_forecast.domain.GetCurrentWeatherUseCase
import com.example.topacademy_android.feature_forecast.domain.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<CalculatorRepository> { CalculatorRepositoryImpl() }
    single { CalculateExpressionUseCase(get()) }

    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single { GetCurrentWeatherUseCase(get()) }
}
