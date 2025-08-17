package com.example.topacademy_android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.topacademy_android.feature_forecast.domain.WeatherRepository
import com.example.topacademy_android.feature_forecast.data.WeatherRepositoryImpl
import com.example.topacademy_android.feature_forecast.presentation.HourlyForecast
import com.example.topacademy_android.feature_forecast.presentation.adapters.HourlyForecastAdapter
import com.example.topacademy_android.feature_forecast.presentation.adapters.WeeklyForecastAdapter
import com.example.topacademy_android.feature_forecast.data.CurrentWeatherResponse
import com.example.topacademy_android.feature_forecast.data.ForecastItem
import com.example.topacademy_android.feature_forecast.data.WeatherResponse
import com.example.topacademy_android.databinding.ActivityMainBinding
import com.example.topacademy_android.feature_forecast.presentation.WeatherViewModel
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private lateinit var weeklyAdapter: WeeklyForecastAdapter

    private val weatherViewModel: WeatherViewModel by lazy {
        WeatherViewModel(WeatherRepositoryImpl.create())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        hourlyAdapter = HourlyForecastAdapter(emptyList())
        weeklyAdapter = WeeklyForecastAdapter(emptyList())

        binding.rvHourly.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.adapter = hourlyAdapter
        binding.rvWeekly.layoutManager = LinearLayoutManager(this)
        binding.rvWeekly.adapter = weeklyAdapter

        val apiKey = "85afd36bff2ab36aac6bdf3a9e0bec09"
        observeWeather()
        weatherViewModel.fetchCurrentWeather("Moscow", apiKey)
        weatherViewModel.fetchForecast("Moscow", apiKey)
    }

    private fun observeWeather() {
        weatherViewModel.currentWeather.observe(this, Observer { currentWeather ->
            currentWeather?.let { updateCurrentWeatherUI(it) }
        })
        weatherViewModel.forecast.observe(this, Observer { forecast ->
            forecast?.let {
                updateHourlyForecast(it)
                updateWeeklyForecast(it)
            }
        })
        weatherViewModel.error.observe(this, Observer { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateCurrentWeatherUI(currentWeather: CurrentWeatherResponse) {
        binding.tvCurrentTemp.text = "${currentWeather.main.temp.toInt()}°"
        binding.tvCurrentCondition.text = currentWeather.weather.firstOrNull()?.description ?: "N/A"
        binding.tvLocation.text = currentWeather.name
        val iconCode = currentWeather.weather.firstOrNull()?.icon
        loadWeatherIcon(binding.imgCurrentWeatherIcon, iconCode)
        binding.tvHumidity.text = "Влажность: ${currentWeather.main.humidity}%"
        binding.tvPressure.text = "Давление: ${currentWeather.main.pressure} гПа"
        binding.tvWindSpeed.text = "Ветер: ${currentWeather.wind.speed} м/с"


        Toast.makeText(
            this,
            "${currentWeather.name}, ${currentWeather.main.temp}°C, ${currentWeather.weather.firstOrNull()?.description}, " +
                    "${currentWeather.main.humidity}%, ${currentWeather.main.pressure} гПа, ${currentWeather.wind.speed} м/с",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun updateHourlyForecast(response: WeatherResponse) {
        val hourlyList: List<HourlyForecast> = response.list.take(5).map {
            ForecastItemToHourly(it)
        }
        hourlyAdapter.updateData(hourlyList)
    }

    private fun updateWeeklyForecast(response: WeatherResponse) {
        val dailyMap = response.list.groupBy { it.dt_txt.substring(0, 10) }

        val weeklyList = dailyMap.map { entry ->
            val day = entry.key
            val temps = entry.value.map { it.main.temp }
            val minTemp = temps.minOrNull()?.toInt() ?: 0
            val maxTemp = temps.maxOrNull()?.toInt() ?: 0
            val iconCode = entry.value.first().weather.firstOrNull()?.icon
            val description = entry.value.first().weather.firstOrNull()?.description

            val precip = entry.value.mapNotNull { it.javaClass.methods.find { m -> m.name == "getRain" }?.invoke(it) }
                .mapNotNull { rainObj ->
                    try {
                        val field = rainObj?.javaClass?.getDeclaredField("_1h")
                        field?.isAccessible = true
                        (field?.get(rainObj) as? Number)?.toInt()
                    } catch (e: Exception) { null }
                }.sum()
            val windAvg = entry.value.mapNotNull { it.javaClass.methods.find { m -> m.name == "getWind" }?.invoke(it) }
                .mapNotNull { windObj ->
                    try {
                        val field = windObj?.javaClass?.getDeclaredField("speed")
                        field?.isAccessible = true
                        (field?.get(windObj) as? Number)?.toDouble()
                    } catch (e: Exception) { null }
                }.average().takeIf { !it.isNaN() } ?: 0.0

            WeeklyForecast(day, minTemp, maxTemp, iconCode, description, precip, windAvg)
        }
        weeklyAdapter.updateData(weeklyList)
    }
}

fun loadWeatherIcon(imageView: android.widget.ImageView, iconCode: String?) {
    if (iconCode == null) return
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    Glide.with(imageView.context).load(iconUrl).into(imageView)
}

fun ForecastItemToHourly(item: ForecastItem): HourlyForecast {
    return HourlyForecast(
        hour = item.dt_txt.substring(11, 16),
        temp = item.main.temp.toInt(),
        iconCode = item.weather.firstOrNull()?.icon
    )
}

data class HourlyForecast(
    val hour: String,
    val temp: Int,
    val iconCode: String?
)

data class WeeklyForecast(
    val day: String,
    val minTemp: Int,
    val maxTemp: Int,
    val iconCode: String?,
    val description: String?,
    val precip: Int?,
    val wind: Double?
)

data class MainWeatherCard(
    val city: String,
    val temp: Int,
    val description: String,
    val iconCode: String?,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double
)
