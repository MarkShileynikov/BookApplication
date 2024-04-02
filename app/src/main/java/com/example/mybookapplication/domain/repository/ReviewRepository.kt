package com.example.mybookapplication.domain.repository

import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    suspend fun postReview(userId: String, username: String, bookId: String, estimation: Int,review: String?) : Event<Review>

    suspend fun fetchReviews(bookId: String) : Flow<List<Review>>
}