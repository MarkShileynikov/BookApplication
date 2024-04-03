package com.example.mybookapplication.domain.repository

import android.net.Uri
import com.example.mybookapplication.domain.entity.Avatar
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.util.Event

interface UpdateUserRepository {

    suspend fun updateUsername(userId: String, username: String): Event<UserProfile>

    suspend fun updateAvatar(uri: Uri, userId: String): Event<UserProfile>

}