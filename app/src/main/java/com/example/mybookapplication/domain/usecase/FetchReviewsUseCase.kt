package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.ReviewRepositoryImpl
import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.repository.ReviewRepository

class FetchReviewsUseCase(private val reviewRepository: ReviewRepository) {

    suspend operator fun invoke(bookId: String) : kotlinx.coroutines.flow.Flow<List<Review>> = reviewRepository.fetchReviews(bookId)
}