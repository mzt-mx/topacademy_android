package com.example.topacademy_android.feature_forecast.data

data class ForecastItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val humidity: Int,
    val pressure: Int
)

data class Weather(
    val description: String?,
    val icon: String?
)
