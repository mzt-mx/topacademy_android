package com.example.topacademy_android.feature_forecast.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topacademy_android.databinding.FragmentWeatherBinding
import com.example.topacademy_android.feature_forecast.data.WeatherResponse
import com.example.topacademy_android.feature_forecast.presentation.adapters.HourlyForecastAdapter
import com.example.topacademy_android.feature_forecast.presentation.adapters.WeeklyForecastAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private lateinit var weeklyAdapter: WeeklyForecastAdapter
    private val viewModel: WeatherViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hourlyAdapter = HourlyForecastAdapter(emptyList())
        weeklyAdapter = WeeklyForecastAdapter(emptyList())
        binding.rvHourly.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.adapter = hourlyAdapter
        binding.rvWeekly.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWeekly.adapter = weeklyAdapter
        viewModel.fetchForecast()
        viewModel.forecast.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                updateHourlyFromForecast(response)
                updateWeeklyFromForecast(response)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { err ->
            if (!err.isNullOrEmpty())
                Toast.makeText(requireContext(), err, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateHourlyFromForecast(response: WeatherResponse) {
        val hourlyList = response.list.map { item ->
            HourlyForecast(
                hour = item.dt_txt.substring(11, 16),
                temp = item.main.temp.toInt(),
                iconCode = item.weather.firstOrNull()?.icon
            )
        }
        hourlyAdapter.updateData(hourlyList)
    }

    private fun updateWeeklyFromForecast(response: WeatherResponse) {
        val dailyMap = response.list.groupBy { it.dt_txt.substring(0, 10) }
        val weeklyList = dailyMap.map { (date, items) ->
            val minTemp = items.minOf { it.main.temp }.toInt()
            val maxTemp = items.maxOf { it.main.temp }.toInt()
            val firstItem = items.first()
            WeeklyForecast(
                day = date,
                minTemp = minTemp,
                maxTemp = maxTemp,
                iconCode = firstItem.weather.firstOrNull()?.icon,
                description = firstItem.weather.firstOrNull()?.description,
                precip = null, // если есть осадки, добавить
                wind = null // если есть ветер, добавить
            )
        }
        weeklyAdapter.updateData(weeklyList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
