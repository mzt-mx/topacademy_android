package com.example.topacademy_android.feature_forecast.data

import com.google.gson.annotations.SerializedName

data class OneCallResponse(
    @SerializedName("hourly") val hourly: List<HourlyForecast>?,
    @SerializedName("daily") val daily: List<DailyForecast>?
)

data class HourlyForecast(
    @SerializedName("dt") val dt: Long,
    @SerializedName("temp") val temp: Double,
    @SerializedName("weather") val weather: List<OneCallWeatherDescription>
)

data class DailyForecast(
    @SerializedName("dt") val dt: Long,
    @SerializedName("temp") val temp: Temp,
    @SerializedName("weather") val weather: List<OneCallWeatherDescription>
)

data class Temp(
    @SerializedName("day") val day: Double,
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double
)

data class OneCallWeatherDescription(
    @SerializedName("main") val main: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?
)
