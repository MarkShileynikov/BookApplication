package com.example.mybookapplication.data.api.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("user_id")
    val userId: String,
    val username: String,
    @SerializedName("book_id")
    val bookId: String,
    val estimation: Int,
    val review: String?,
    @SerializedName("created")
    val publicationDate: Long,
)
