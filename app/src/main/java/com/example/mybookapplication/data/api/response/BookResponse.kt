package com.example.mybookapplication.data.api.response

import com.google.gson.annotations.SerializedName

data class BookResponse(
    val objectId : String,
    val title: String,
    val author: String,
    val genre: String,
    val description: String,
    @SerializedName("release_year")
    val releaseYear: Int,
    @SerializedName("age_limit")
    val ageLimit: Int,
    val cover: String,
    val pages: Int
)
