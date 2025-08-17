package com.example.topacademy_android.feature_forecast.data

import com.example.topacademy_android.feature_forecast.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/forecast")
    suspend fun getForecastAsync(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}

