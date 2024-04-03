package com.example.mybookapplication.data.api.request

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    @SerializedName("user_id")
    val userId: String,
    val username: String,
    @SerializedName("book_id")
    val bookId: String,
    val estimation: Int,
    val review: String?,
    val avatar: String?
)
