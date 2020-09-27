package com.robusta.photoweatherapp.presentation.repository.datalayer.repository



data class ResponseResource<out T>(val status: Int, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): ResponseResource<T>? {
            return ResponseResource(200, data, null)
        }

        fun <T> error(
            msg: String,
            data: T?,
            status: Int
        ): ResponseResource<T> {
            return ResponseResource(status, data, msg)
        }
    }
}