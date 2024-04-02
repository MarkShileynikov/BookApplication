package com.example.mybookapplication.domain.usecase

import android.content.Context
import com.example.mybookapplication.R
import com.example.mybookapplication.data.repository.SessionRepositoryImpl
import com.example.mybookapplication.domain.entity.Session
import com.example.mybookapplication.domain.repository.SessionRepository
import com.example.mybookapplication.domain.util.Event
import com.example.mybookapplication.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInUseCase(private val context : Context, private val sessionRepository: SessionRepository) : UseCase<SignInUseCase.Params, Session> {

    data class Params(
        val email: String,
        val password: String,
    )

    override suspend fun invoke(params: Params): Flow<Session> = flow {
        val email = params.email
        val password = params.password
        if (email.isNotBlank() && password.isNotBlank()) {
            val event =
                sessionRepository.signIn(email = email, password = password)
            when(event) {
                is Event.Success -> {
                    val session = event.data
                    sessionRepository.saveToken(session.token)
                    sessionRepository.saveUserProfile(session.userProfile)
                    emit(session)
                }
                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        } else {
            throw Exception(context.getString(R.string.fill_all_fields))
        }
    }
}