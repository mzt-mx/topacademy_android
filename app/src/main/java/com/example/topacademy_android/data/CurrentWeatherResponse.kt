package com.example.topacademy_android.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeatherResponse(
    val weather: List<WeatherDescription>,
    val main: Main,
    val name: String
) : Parcelable

@Parcelize
data class WeatherDescription(
    val description: String,
    val icon: String
) : Parcelable

