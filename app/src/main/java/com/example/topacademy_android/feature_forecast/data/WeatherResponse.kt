package com.example.topacademy_android.feature_forecast.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class WeatherResponse(
    val list: List<ForecastItem>,
    val city: City
) : Parcelable

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coord
) : Parcelable

@Parcelize
data class Coord(
    val lat: Double,
    val lon: Double
) : Parcelable

@Parcelize
data class ForecastItem(
    val dt_txt: String,
    val main: ForecastMain,
    val weather: List<@RawValue ForecastWeather>
) : Parcelable

@Parcelize
data class ForecastMain(
    val temp: Double
) : Parcelable

@Parcelize
data class ForecastWeather(
    val description: String,
    val icon: String
) : Parcelable
