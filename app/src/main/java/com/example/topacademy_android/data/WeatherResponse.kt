package com.example.topacademy_android.data

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
    val main: Main,
    val weather: List<@RawValue Weather>
) : Parcelable

@Parcelize
data class Main(
    val temp: Double
) : Parcelable

@Parcelize
data class Weather(
    val description: String,
    val icon: String
) : Parcelable
