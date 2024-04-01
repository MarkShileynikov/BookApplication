package com.example.mybookapplication.data.api

import com.example.mybookapplication.data.api.request.ReviewRequest
import com.example.mybookapplication.data.api.response.ReviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReviewApiService {

    @POST("data/reviews")
    suspend fun postReview(@Body request: ReviewRequest): Response<ReviewResponse>

    @GET("data/reviews")
    suspend fun fetchReviews(@Query("where") bookId: String): List<ReviewResponse>
}