package com.robusta.photoweatherapp.presentation.repository.datalayer.repository

import androidx.lifecycle.MutableLiveData
import com.robusta.photoweatherapp.R
import com.robusta.photoweatherapp.presentation.repository.datalayer.model.weather.WeatherResponse
import com.robusta.photoweatherapp.utilities.App
import com.robusta.photoweatherapp.utilities.RemoteConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionApi @Inject constructor(private val apiServices: ClientApiServices) {

    fun getWeather(lat: String, lon: String): MutableLiveData<ResponseResource<WeatherResponse>> {
        val data = MutableLiveData<ResponseResource<WeatherResponse>>()

        apiServices.getWeather(lat, lon, RemoteConfiguration.APP_ID).enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {

                data.value = ResponseHandler.responseBody(response)
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                data.value = ResponseResource.error(
                    App.context!!.resources.getString(R.string.something_wrong),
                    null,
                    500
                )
            }

        })
        return data
    }
}