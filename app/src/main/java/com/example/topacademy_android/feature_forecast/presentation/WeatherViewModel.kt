package com.example.topacademy_android.feature_forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.topacademy_android.feature_forecast.domain.WeatherRepository
import com.example.topacademy_android.feature_forecast.data.CurrentWeatherResponse
import com.example.topacademy_android.feature_forecast.data.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse> = _currentWeather

    private val _forecast = MutableLiveData<WeatherResponse>()
    val forecast: LiveData<WeatherResponse> = _forecast

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchCurrentWeather(city: String, units: String = "metric", lang: String = "en") {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(city, units, lang)
                _currentWeather.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun fetchForecast(city: String = "Москва", units: String = "metric", lang: String = "ru") {
        viewModelScope.launch {
            try {
                val response = repository.getForecast(city, units, lang)
                _forecast.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
