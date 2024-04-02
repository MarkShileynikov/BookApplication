package com.example.mybookapplication.domain.repository

import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.util.Event

interface UpdateUserRepository {

    suspend fun updateUsername(userId: String, username : String) : Event<UserProfile>
}