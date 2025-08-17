package com.example.topacademy_android.feature_forecast.domain

import com.example.topacademy_android.feature_forecast.data.CurrentWeatherResponse
import kotlin.Result

class GetCurrentWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(
        city: String,
        apiKey: String,
        units: String = "metric",
        lang: String = "en"
    ): Result<CurrentWeatherResponse> = try {
        Result.success(repository.getCurrentWeather(city, apiKey, units, lang))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
