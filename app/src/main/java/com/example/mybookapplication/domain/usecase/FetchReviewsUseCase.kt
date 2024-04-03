package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchReviewsUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {

    suspend operator fun invoke(bookId: String) : Flow<List<Review>> = reviewRepository.fetchReviews(bookId)
}