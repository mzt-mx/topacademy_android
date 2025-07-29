package com.example.topacademy_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.topacademy_android.adapter.HourlyForecastAdapter
import com.example.topacademy_android.adapter.WeeklyForecastAdapter
import com.example.topacademy_android.data.CurrentWeatherResponse
import com.example.topacademy_android.data.ForecastItem
import com.example.topacademy_android.data.WeatherResponse
import com.example.topacademy_android.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private lateinit var weeklyAdapter: WeeklyForecastAdapter

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

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

        fetchWeather()
    }

    private fun fetchWeather() {
        coroutineScope.launch {
            try {
                val service = RetrofitInstance.api

                val currentWeatherDeferred = async(Dispatchers.IO) {
                    service.getCurrentWeather(
                        lat = 55.7558,
                        lon = 37.6173,
                        appId = "85afd36bff2ab36aac6bdf3a9e0bec09",
                        units = "metric"
                    )
                }

                val forecastDeferred = async(Dispatchers.IO) {
                    service.getFiveDayForecast(
                        lat = 55.7558,
                        lon = 37.6173,
                        appId = "85afd36bff2ab36aac6bdf3a9e0bec09",
                        units = "metric"
                    )
                }

                val currentWeather = currentWeatherDeferred.await()
                val forecast = forecastDeferred.await()

                updateCurrentWeatherUI(currentWeather)
                updateHourlyForecast(forecast)
                updateWeeklyForecast(forecast)

            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateCurrentWeatherUI(currentWeather: CurrentWeatherResponse) {
        binding.tvCurrentTemp.text = "${currentWeather.main.temp.toInt()}°"
        binding.tvCurrentCondition.text = currentWeather.weather.firstOrNull()?.description ?: "N/A"
        binding.tvLocation.text = currentWeather.name

        val iconCode = currentWeather.weather.firstOrNull()?.icon
        loadWeatherIcon(binding.imgCurrentWeatherIcon, iconCode)
    }

    private fun updateHourlyForecast(response: WeatherResponse) {
        val hourlyList = response.list.take(5).map {
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

            WeeklyForecast(day, minTemp, maxTemp, iconCode)
        }
        weeklyAdapter.updateData(weeklyList)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}

// Retrofit API interface

interface WeatherApi {
    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): WeatherResponse

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): CurrentWeatherResponse
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}

// Helper function to load icon with Glide

fun loadWeatherIcon(imageView: android.widget.ImageView, iconCode: String?) {
    if (iconCode == null) return
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    Glide.with(imageView.context)
        .load(iconUrl)
        .into(imageView)
}

// Conversion function for hourly forecast

fun ForecastItemToHourly(item: ForecastItem): HourlyForecast {
    return HourlyForecast(
        hour = item.dt_txt.substring(11, 16),
        temp = item.main.temp.toInt(),
        iconCode = item.weather.firstOrNull()?.icon
    )
}

// Models for adapters

data class HourlyForecast(
    val hour: String,
    val temp: Int,
    val iconCode: String?
)

data class WeeklyForecast(
    val day: String,
    val minTemp: Int,
    val maxTemp: Int,
    val iconCode: String?
)
