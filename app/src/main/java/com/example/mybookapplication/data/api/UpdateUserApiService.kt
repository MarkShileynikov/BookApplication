package com.example.mybookapplication.data.api

import com.example.mybookapplication.data.api.request.UpdateUsernameRequest
import com.example.mybookapplication.data.api.response.AvatarResponse
import com.example.mybookapplication.data.api.response.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UpdateUserApiService {
    @PUT("users/{objectId}")
    suspend fun updateUsername(@Path("objectId") id: String, @Body request: UpdateUsernameRequest) : Response<UserProfileResponse>

    @Multipart
    @POST("files")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part) : Response<AvatarResponse>
}