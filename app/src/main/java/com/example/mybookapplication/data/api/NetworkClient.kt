package com.example.mybookapplication.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private const val BASE_NOTE_URL = "utmostback.backendless.app/api/data/"

    private val logging = HttpLoggingInterceptor()

    init {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_NOTE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        )
        .build()
}