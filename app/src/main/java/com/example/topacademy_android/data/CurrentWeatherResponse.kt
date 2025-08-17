package com.example.topacademy_android.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeatherResponse(
    val weather: List<WeatherDescription>,
    val main: CurrentMain,
    val wind: Wind, // добавлено поле wind
    val name: String
) : Parcelable

@Parcelize
data class WeatherDescription(
    val description: String,
    val icon: String
) : Parcelable

@Parcelize
data class CurrentMain(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
) : Parcelable

@Parcelize
data class Wind(
    val speed: Double
) : Parcelable
