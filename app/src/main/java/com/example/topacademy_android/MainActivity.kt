package com.example.topacademy_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.topacademy_android.databinding.ActivityMainBinding
import com.example.topacademy_android.feature_forecast.presentation.adapters.HourlyForecastAdapter
import com.example.topacademy_android.feature_forecast.presentation.adapters.WeeklyForecastAdapter
import com.example.topacademy_android.feature_forecast.data.CurrentWeatherResponse
import com.example.topacademy_android.feature_forecast.data.WeatherResponse
import com.example.topacademy_android.feature_forecast.presentation.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private lateinit var weeklyAdapter: WeeklyForecastAdapter

    private val viewModel: WeatherViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        hourlyAdapter = HourlyForecastAdapter(emptyList())
        weeklyAdapter = WeeklyForecastAdapter(emptyList())
        binding.rvHourly.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.adapter = hourlyAdapter
        binding.rvWeekly.layoutManager = LinearLayoutManager(this)
        binding.rvWeekly.adapter = weeklyAdapter

        viewModel.fetchForecast()
        viewModel.forecast.observe(this) { response ->
            if (response != null) {
                updateHourlyFromForecast(response)
                updateWeeklyFromForecast(response)
            }
        }
        viewModel.error.observe(this) { err ->
            if (!err.isNullOrEmpty())
                Toast.makeText(this, err, Toast.LENGTH_LONG).show()
        }
        // Получаем и отображаем текущую погоду для Москвы
        viewModel.fetchCurrentWeather("Москва", units = "metric", lang = "ru")
        viewModel.currentWeather.observe(this) { currentWeather ->
            if (currentWeather != null) {
                updateCurrentWeatherUI(currentWeather)
            }
        }
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

    private fun loadWeatherIcon(imageView: android.widget.ImageView, iconCode: String?) {
        if (iconCode == null) return
        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
        Glide.with(imageView.context).load(iconUrl).into(imageView)
    }

    private fun updateHourlyFromForecast(response: WeatherResponse) {
        val sdfInput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val sdfOutput = SimpleDateFormat("HH:mm", Locale.getDefault())
        val list = response.list.take(12).map {
            val date = sdfInput.parse(it.dt_txt)
            com.example.topacademy_android.feature_forecast.presentation.HourlyForecast(
                hour = if (date != null) sdfOutput.format(date) else it.dt_txt,
                temp = it.main.temp.toInt(),
                iconCode = it.weather.firstOrNull()?.icon
            )
        }
        hourlyAdapter.updateData(list)
    }

    private fun updateWeeklyFromForecast(response: WeatherResponse) {
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        val grouped = response.list.groupBy { it.dt_txt.substring(0, 10) }
        val list = grouped.map { (date, items) ->
            val minTemp = items.minOfOrNull { it.main.temp }?.toInt() ?: 0
            val maxTemp = items.maxOfOrNull { it.main.temp }?.toInt() ?: 0
            val iconCode = items.firstOrNull()?.weather?.firstOrNull()?.icon
            val description = items.firstOrNull()?.weather?.firstOrNull()?.description
            com.example.topacademy_android.feature_forecast.presentation.WeeklyForecast(
                day = sdf.format(java.text.SimpleDateFormat("yyyy-MM-dd").parse(date)!!),
                minTemp = minTemp,
                maxTemp = maxTemp,
                iconCode = iconCode,
                description = description,
                precip = null,
                wind = null
            )
        }.take(7)
        weeklyAdapter.updateData(list)
    }
}
