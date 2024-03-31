package com.example.mybookapplication.data.repository

import android.content.Context
import com.example.mybookapplication.data.api.UpdateUserApiService
import com.example.mybookapplication.data.api.request.UserProfileRequest
import com.example.mybookapplication.data.api.util.doCall
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.repository.UpdateUserRepository
import com.example.mybookapplication.domain.util.Event

class UpdateUserRepositoryImpl(private val context : Context, private val updateUserApiService: UpdateUserApiService) : UpdateUserRepository{
    override suspend fun updateUser(user: UserProfile): Event<UserProfile> {
        val event = doCall(context) {
            val request = UserProfileRequest(
                id = user.userId,
                username = user.username,
                email = user.email,
                avatar = user.avatar
            )
            return@doCall updateUserApiService.updateUser(request)
        }
        return when(event) {
            is Event.Success -> {
                val response = event.data
                val user = UserProfile(
                    userId = response.id,
                    username = response.username,
                    email = response.email,
                    avatar = response.avatar,
                )
                Event.Success(user)
            }
            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}