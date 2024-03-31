package com.example.mybookapplication.data.api

import com.example.mybookapplication.data.api.request.SignInRequest
import com.example.mybookapplication.data.api.request.SignUpRequest
import com.example.mybookapplication.data.api.request.UserProfileRequest
import com.example.mybookapplication.data.api.response.SignInResponse
import com.example.mybookapplication.data.api.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {
    @POST("users/login")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("users/register")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

}