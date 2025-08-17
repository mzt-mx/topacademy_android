package com.example.topacademy_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.topacademy_android.R
import com.example.topacademy_android.data.ForecastItem

class ForecastAdapter(
    private var items: List<ForecastItem>,
    private val onItemClick: (ForecastItem) -> Unit
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)

        fun bind(item: ForecastItem) {
            tvDate.text = item.dt_txt
            tvTemp.text = "${item.main.temp.toInt()} °C"
            tvDescription.text = item.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: ""

            val iconCode = item.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
            ivIcon.load(iconUrl)

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<ForecastItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
