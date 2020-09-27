package com.robusta.photoweatherapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.robusta.photoweatherapp.presentation.repository.datalayer.model.weather.WeatherResponse
import com.robusta.photoweatherapp.presentation.repository.datalayer.repository.ConnectionApi
import com.robusta.photoweatherapp.presentation.repository.datalayer.repository.ResponseResource

/**
 * Created by Ahmed Mostafa on 9/23/2020.
 */
class WeatherVM: ViewModel() {
    var response: LiveData<ResponseResource<WeatherResponse>>? = null
    private var connectionApi: ConnectionApi? = null


    fun init(connectionApi: ConnectionApi?) {
        if (connectionApi != null) {
            this.connectionApi = connectionApi
        }
    }

    fun getWeather(lat: String, lon: String){
        response = connectionApi!!.getWeather(lat, lon)
    }
}