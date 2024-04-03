package com.example.mybookapplication.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClientConfig {

    const val BASE_NOTE_URL = "https://utmostback.backendless.app/api/"
}