package com.example.topacademy_android.feature_forecast.presentation

import com.example.topacademy_android.feature_forecast.data.ForecastItem

// Преобразование ForecastItem в HourlyForecast
fun ForecastItemToHourly(item: ForecastItem): HourlyForecast {
    return HourlyForecast(
        hour = item.dt_txt.substring(11, 16),
        temp = item.main.temp.toInt(),
        iconCode = item.weather.firstOrNull()?.icon
    )
}

