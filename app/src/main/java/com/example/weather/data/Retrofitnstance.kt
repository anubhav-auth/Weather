package com.example.weather.data

import com.example.weather.Presentation.IpService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofitnstance {
    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client: OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()

    val api: Api = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Api.BASE_URL)
            .client(
                client
            ).build().create(Api::class.java)

    val ipService = Retrofit.Builder()
        .baseUrl("https://api.ipify.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IpService::class.java)

}

