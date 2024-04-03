package com.example.mybookapplication.data.repository

import android.content.Context
import com.example.mybookapplication.data.api.ReviewApiService
import com.example.mybookapplication.data.api.request.ReviewRequest
import com.example.mybookapplication.data.api.util.doCall
import com.example.mybookapplication.domain.entity.Review
import com.example.mybookapplication.domain.repository.ReviewRepository
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val context: Context, private val reviewApiService: ReviewApiService) : ReviewRepository {

    override suspend fun postReview(userId: String, usermame: String, bookId: String, estimation: Int,review: String?): Event<Review> {
        val event = doCall(context) {
            return@doCall reviewApiService.postReview(
                ReviewRequest(
                    userId = userId,
                    username = usermame,
                    bookId = bookId,
                    estimation = estimation,
                    review = review,
                )
            )
        }

        return when(event) {
            is Event.Success -> {
                val response = event.data
                val review = Review(
                    userId = response.userId,
                    username = response.username,
                    bookId = response.bookId,
                    estimation = response.estimation,
                    review = response.review,
                    publicationDate = response.publicationDate
                )
                Event.Success(review)
            }
            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchReviews(bookId: String): Flow<List<Review>> =  flow {
        val response = reviewApiService.fetchReviews("book_id = '$bookId'")
        emit(response)
    }.map { reponses ->
        reponses.map { response ->
            Review(
                userId = response.userId,
                username = response.username,
                bookId = response.bookId,
                estimation = response.estimation,
                review = response.review,
                publicationDate = response.publicationDate
            )
        }
    }
}
