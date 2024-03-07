package com.example.mybookapplication.data.api

import com.example.mybookapplication.data.api.response.BookResponse
import retrofit2.http.GET

interface BookApiService {

    @GET("Books")
    suspend fun fetchBooks() : List<BookResponse>
}