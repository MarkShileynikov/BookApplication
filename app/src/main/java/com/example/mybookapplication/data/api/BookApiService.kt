package com.example.mybookapplication.data.api

import com.example.mybookapplication.data.api.response.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {

    @GET("data/books")
    suspend fun fetchBooks() : List<BookResponse>

    @GET("data/books")
    suspend fun fetchBooksByGenre(@Query("where") bookGenre : String) : List<BookResponse>

    @GET("data/books")
    suspend fun fetchBooksByTitleOrAuthor(@Query("where") titleOrAuthor : String) : List<BookResponse>
}