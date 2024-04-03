package com.example.mybookapplication.data.repository

import android.content.Context
import android.net.Uri
import com.example.mybookapplication.data.api.NetworkClientConfig
import com.example.mybookapplication.data.api.UpdateUserApiService
import com.example.mybookapplication.data.api.request.UpdateAvatarRequest
import com.example.mybookapplication.data.api.request.UpdateUsernameRequest
import com.example.mybookapplication.data.api.response.UserProfileResponse
import com.example.mybookapplication.data.api.util.doCall
import com.example.mybookapplication.data.utils.ImageUtils
import com.example.mybookapplication.domain.entity.Avatar
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.repository.UpdateUserRepository
import com.example.mybookapplication.domain.util.Event
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject


class UpdateUserRepositoryImpl @Inject constructor(
    private val context : Context,
    private val updateUserApiService: UpdateUserApiService,
) : UpdateUserRepository{
    override suspend fun updateUsername(userId: String, username: String): Event<UserProfile> {
        val event = doCall(context) {
            return@doCall updateUserApiService.updateUsername(userId, UpdateUsernameRequest(username))
        }

        return when(event) {
            is Event.Success -> {
                val response = event.data
                val user = transformToUserProfile(response)
                Event.Success(user)
            }
            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }



    override suspend fun updateAvatar(fileUri: Uri, userId: String): Event<UserProfile> {

        val compressedFile = ImageUtils.compressImage(context, fileUri)

        val requestBody = compressedFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())

        if (requestBody != null) {
            val body = MultipartBody.Part.createFormData("picture", compressedFile.name, requestBody)
            val event = doCall(context) {
                return@doCall updateUserApiService.uploadAvatar(body)
            }
            return when (event) {
                is Event.Success -> {
                    val filePath = event.data.filePath
                    val url = "${NetworkClientConfig.BASE_NOTE_URL}/files/$filePath"
                    val event = doCall(context) {
                        return@doCall updateUserApiService.updateAvatar(userId, UpdateAvatarRequest(url))
                    }
                    when (event) {
                        is Event.Success -> {
                            val response = event.data
                            val user = transformToUserProfile(response)
                            Event.Success(user)
                        }
                        is Event.Failure -> {
                            Event.Failure(event.exception)
                        }
                    }
                }
                is Event.Failure -> {
                    val error = event.exception
                    Event.Failure(error)
                }
            }
        } else {
            return Event.Failure("No such file or directory")
        }
    }

    private fun transformToUserProfile(response: UserProfileResponse) : UserProfile {
        val user = UserProfile(
            userId = response.id,
            username = response.username,
            email = response.email,
            avatar = response.avatar,
        )
        return user
    }

}