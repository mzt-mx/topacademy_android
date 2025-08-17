package com.example.topacademy_android.feature_forecast.domain

import com.example.topacademy_android.feature_forecast.data.CurrentWeatherResponse
import com.example.topacademy_android.feature_forecast.data.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(
        city: String,
        units: String = "metric",
        lang: String = "en"
    ): CurrentWeatherResponse

    suspend fun getForecast(
        city: String,
        units: String = "metric",
        lang: String = "en"
    ): WeatherResponse
}
