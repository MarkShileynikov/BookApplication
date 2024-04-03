package com.example.mybookapplication.data.repository

import android.content.Context
import com.example.mybookapplication.data.api.AuthApiService
import com.example.mybookapplication.data.api.request.SignInRequest
import com.example.mybookapplication.data.api.request.SignUpRequest
import com.example.mybookapplication.data.api.util.doCall
import com.example.mybookapplication.data.prefs.PrefsDataSource
import com.example.mybookapplication.domain.entity.Session
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.repository.SessionRepository
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val context : Context,
    private val authApiService: AuthApiService,
    private val prefsDataSource: PrefsDataSource
) : SessionRepository {

    override suspend fun signIn(email: String, password: String): Event<Session> {
        val event = doCall(context){
            val request = SignInRequest(email, password)
            return@doCall authApiService.signIn(request)
        }
        return when(event) {
            is Event.Success -> {
                val response = event.data
                val session = Session(
                    token = response.token,
                    userProfile = UserProfile(
                        userId = response.id,
                        email = response.email,
                        username = response.username,
                        avatar = response.avatar
                    )
                )
                Event.Success(session)
            }
            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun signUp(email: String, password: String, username: String): Event<Session> {
        val event = doCall(context) {
            val request = SignUpRequest(email = email, password = password, username = username)
            return@doCall authApiService.signUp(request)
        }

        return when(event) {
            is Event.Success -> {
                val response = event.data
                val session = Session(
                    token = "",
                    userProfile = UserProfile(
                        userId = response.id,
                        email = response.email,
                        username = response.username,
                        avatar = null
                    )
                )
                Event.Success(session)
            }
            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchToken(): Flow<String?> = flow {
        val token = prefsDataSource.fetchToken()
        emit(token)
    }

    override suspend fun saveToken(token: String) = prefsDataSource.saveToken(token)

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        prefsDataSource.saveUserProfile(userProfile)
    }
}