package com.example.topacademy_android.feature_forecast.data

import com.example.topacademy_android.feature_forecast.data.ApiKeys
import com.example.topacademy_android.feature_forecast.domain.WeatherRepository

class WeatherRepositoryImpl(private val api: WeatherApi) : WeatherRepository {
    override suspend fun getCurrentWeather(city: String, units: String, lang: String): CurrentWeatherResponse {
        return api.getCurrentWeather(city, units, lang, ApiKeys.OPEN_WEATHER)
    }

    override suspend fun getForecast(city: String, units: String, lang: String): WeatherResponse {
        return api.getForecast(city, units, lang, ApiKeys.OPEN_WEATHER)
    }
}
