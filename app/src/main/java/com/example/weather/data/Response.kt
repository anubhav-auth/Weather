package com.example.weather.data

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Response<T>(data)
    class Error<T>(data: T? = null, message: String?) : Response<T>(data, message)

}