package com.robusta.photoweatherapp.presentation.repository.datalayer.repository

import com.robusta.photoweatherapp.presentation.repository.datalayer.model.weather.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientApiServices {

    @GET("weather")
    fun getWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appId: String?
    ): Call<WeatherResponse>

}
