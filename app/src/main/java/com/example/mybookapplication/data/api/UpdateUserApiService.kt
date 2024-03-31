package com.example.mybookapplication.data.api

import com.example.mybookapplication.data.api.request.UserProfileRequest
import com.example.mybookapplication.data.api.response.UserProfileResponse
import com.example.mybookapplication.domain.util.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface UpdateUserApiService {
    @PUT("users")
    suspend fun updateUser(@Body request: UserProfileRequest) : Response<UserProfileResponse>
}