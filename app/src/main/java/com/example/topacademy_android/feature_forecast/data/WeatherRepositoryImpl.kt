package com.example.topacademy_android.feature_forecast.data

import com.example.topacademy_android.feature_forecast.domain.WeatherRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherRepositoryImpl(private val api: WeatherApi) : com.example.topacademy_android.feature_forecast.domain.WeatherRepository {
    override suspend fun getCurrentWeather(city: String, units: String, lang: String): CurrentWeatherResponse {
        return api.getCurrentWeather(city, units, lang)
    }

    override suspend fun getForecast(city: String, units: String, lang: String): WeatherResponse {
        return api.getForecast(city, units, lang)
    }

    companion object {
        fun create(): WeatherRepositoryImpl {
            val apiKey = ApiKeys.OPEN_WEATHER
            val interceptor = Interceptor { chain ->
                val original = chain.request()
                val originalUrl = original.url
                val url = originalUrl.newBuilder()
                    .addQueryParameter("appid", apiKey)
                    .build()
                val requestBuilder = original.newBuilder().url(url)
                chain.proceed(requestBuilder.build())
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val api = retrofit.create(WeatherApi::class.java)
            return WeatherRepositoryImpl(api)
        }
    }
}
