package com.example.mybookapplication.data.api

import com.example.mybookapplication.data.api.request.UpdateUsernameRequest
import com.example.mybookapplication.data.api.response.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UpdateUserApiService {
    @PUT("users/{objectId}")
    suspend fun updateUsername(@Path("objectId") id: String, @Body request: UpdateUsernameRequest) : Response<UserProfileResponse>
}