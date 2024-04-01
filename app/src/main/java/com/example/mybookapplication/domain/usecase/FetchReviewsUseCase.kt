package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.ReviewRepositoryImpl
import com.example.mybookapplication.domain.entity.Review

class FetchReviewsUseCase(private val reviewRepository: ReviewRepositoryImpl) {
    suspend operator fun invoke(bookId: String) : kotlinx.coroutines.flow.Flow<List<Review>> = reviewRepository.fetchReviews(bookId)
}