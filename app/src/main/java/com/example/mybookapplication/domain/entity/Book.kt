package com.example.mybookapplication.domain.entity
data class Book(
    val id : String,
    val title: String,
    val author: String,
    val genre: String,
    val description: String,
    val releaseYear: Int,
    val ageLimit: Int,
    val cover: String,
    val pages: Int
)
