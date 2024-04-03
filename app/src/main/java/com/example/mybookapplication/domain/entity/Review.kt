package com.example.mybookapplication.domain.entity

data class Review(
    val userId: String,
    val username: String,
    val bookId: String,
    val estimation: Int,
    val review: String?,
    val publicationDate: Long,
    val avatar: String?
)
