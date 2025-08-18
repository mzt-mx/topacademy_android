package com.example.topacademy_android.feature_forecast.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class WeatherResponse(
    val list: @RawValue List<ForecastItem>,
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
