package com.example.topacademy_android.feature_forecast.presentation

data class WeeklyForecast(
    val day: String,
    val minTemp: Int,
    val maxTemp: Int,
    val iconCode: String?,
    val description: String?,
    val precip: Int?,
    val wind: Double?
)

