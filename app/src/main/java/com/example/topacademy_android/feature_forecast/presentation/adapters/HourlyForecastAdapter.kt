package com.example.topacademy_android.feature_forecast.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.topacademy_android.databinding.ItemHourlyForecastBinding
import com.example.topacademy_android.feature_forecast.presentation.HourlyForecast

class HourlyForecastAdapter(private var data: List<HourlyForecast>) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder>() {

    inner class HourlyViewHolder(val binding: ItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = ItemHourlyForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val item = data[position]
        holder.binding.tvHour.text = item.hour
        holder.binding.tvTemp.text = "${item.temp}°"

        val iconCode = item.iconCode
        if (iconCode != null) {
            val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
            Glide.with(holder.binding.imgIcon.context)
                .load(iconUrl)
                .into(holder.binding.imgIcon)
        } else {
            holder.binding.imgIcon.setImageResource(0)
        }
    }

    override fun getItemCount() = data.size

    fun updateData(newData: List<HourlyForecast>) {
        data = newData
        notifyDataSetChanged()
    }
}

