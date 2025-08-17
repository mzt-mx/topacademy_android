package com.example.topacademy_android.feature_forecast.domain

import com.example.topacademy_android.feature_forecast.data.CurrentWeatherResponse
import com.example.topacademy_android.feature_forecast.data.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(
        city: String,
        apiKey: String,
        units: String = "metric",
        lang: String = "en"
    ): CurrentWeatherResponse

    suspend fun getForecast(
        city: String,
        apiKey: String,
        units: String = "metric",
        lang: String = "en"
    ): WeatherResponse
}

