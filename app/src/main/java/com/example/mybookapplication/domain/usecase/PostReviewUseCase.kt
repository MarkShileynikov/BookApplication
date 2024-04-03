package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.repository.ReviewRepository
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {

    data class Params(
        val userId: String,
        val username: String,
        val bookId: String,
        val estimation: Int,
        val review: String?,
        val avatar: String?
    )

    operator fun invoke(params: Params) : Flow<Review> = flow {
        val event = reviewRepository.postReview(
            userId = params.userId,
            username = params.username,
            bookId = params.bookId,
            estimation = params.estimation,
            review = params.review,
            avatar = params.avatar

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