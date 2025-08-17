package com.example.topacademy_android.feature_forecast.presentation

data class HourlyForecast(
    val hour: String,
    val temp: Int,
    val iconCode: String?
)

