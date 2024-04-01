package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.ReviewRepositoryImpl
import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostReviewUseCase(private val reviewRepository: ReviewRepositoryImpl) {
    data class Params(
        val userId: String,
        val username: String,
        val bookId: String,
        val estimation: Int,
        val review: String?,
    )
    operator fun invoke(params: Params) : Flow<Review> = flow {
        val event = reviewRepository.postReview(
            userId = params.userId,
            usermame = params.username,
            bookId = params.bookId,
            estimation = params.estimation,
            review = params.review
        )
        when(event) {
            is Event.Success -> {
                val review = event.data
                emit(review)
            }
            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }
}