package com.example.mybookapplication.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NetworkClient {
    private const val BASE_NOTE_URL = "https://utmostback.backendless.app/api/"

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

    fun provideBookApiService(): BookApiService =
        retrofit.create(BookApiService::class.java)

    fun provideAuthApiService(): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    fun provideUpdateUserApiService() : UpdateUserApiService =
        retrofit.create(UpdateUserApiService::class.java)
}