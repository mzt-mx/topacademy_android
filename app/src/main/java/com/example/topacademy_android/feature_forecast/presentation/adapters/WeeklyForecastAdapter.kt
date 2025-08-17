package com.example.topacademy_android.feature_forecast.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.topacademy_android.databinding.ItemWeeklyForecastBinding
import com.example.topacademy_android.WeeklyForecast

class WeeklyForecastAdapter(private var data: List<WeeklyForecast>) :
    RecyclerView.Adapter<WeeklyForecastAdapter.WeeklyViewHolder>() {

    inner class WeeklyViewHolder(val binding: ItemWeeklyForecastBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        val binding = ItemWeeklyForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return WeeklyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        val item = data[position]
        holder.binding.tvDay.text = item.day
        holder.binding.tvTempRange.text = "${item.minTemp}° / ${item.maxTemp}°"
        holder.binding.tvDescription.text = item.description?.replaceFirstChar { it.uppercase() } ?: ""
        holder.binding.tvPrecip.text = if (item.precip != null) "Осадки: ${item.precip}%" else ""
        holder.binding.tvWind.text = if (item.wind != null) "Ветер: ${"%.1f".format(item.wind)} м/с" else ""

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

    fun updateData(newData: List<WeeklyForecast>) {
        data = newData
        notifyDataSetChanged()
    }
}

