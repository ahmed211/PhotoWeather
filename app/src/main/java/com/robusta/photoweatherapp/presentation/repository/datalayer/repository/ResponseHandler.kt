package com.robusta.photoweatherapp.presentation.repository.datalayer.repository

import com.google.gson.Gson
import com.robusta.photoweatherapp.R
import com.robusta.photoweatherapp.presentation.repository.datalayer.model.ErrorNetwork
import com.robusta.photoweatherapp.utilities.App
import retrofit2.Response

object ResponseHandler {

    fun <T> responseBody(response: Response<T>): ResponseResource<T>? {
        when {
            response.isSuccessful -> {
                return ResponseResource.success(response.body())
            }
            response.code() in 400..500 -> {
                val errorResponse = Gson().fromJson(
                    response.errorBody()!!.charStream(), ErrorNetwork::class.java
                )

                return ResponseResource.error(
                    errorResponse.message,
                    null, errorResponse.cod
                )
            }
            else -> {
                return ResponseResource.error(
                    App.context!!.resources.getString(R.string.something_wrong),
                    null,
                    422
                )
            }

        }
    }

}