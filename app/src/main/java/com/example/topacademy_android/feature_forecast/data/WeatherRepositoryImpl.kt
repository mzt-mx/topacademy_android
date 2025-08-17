package com.example.topacademy_android.feature_forecast.data

import com.example.topacademy_android.feature_forecast.domain.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherRepositoryImpl(private val api: WeatherApi) : com.example.topacademy_android.feature_forecast.domain.WeatherRepository {
    override suspend fun getCurrentWeather(city: String, apiKey: String, units: String, lang: String): CurrentWeatherResponse {
        return api.getCurrentWeather(city, apiKey, units, lang)
    }

    override suspend fun getForecast(city: String, apiKey: String, units: String, lang: String): WeatherResponse {
        return api.getForecast(city, apiKey, units, lang)
    }

    companion object {
        fun create(): WeatherRepositoryImpl {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val api = retrofit.create(WeatherApi::class.java)
            return WeatherRepositoryImpl(api)
        }
    }
}

