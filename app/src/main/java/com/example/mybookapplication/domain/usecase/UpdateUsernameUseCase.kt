package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.prefs.PrefsDataSource
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import com.example.mybookapplication.data.repository.UpdateUserRepositoryImpl
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.repository.UpdateUserRepository
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateUsernameUseCase(
    private val prefsDataSource: PrefsDataSource,
    private val updateUserRepository: UpdateUserRepository
) {

    data class Params(
        val userId: String,
        val username: String,
    )

    operator fun invoke(params: Params) : Flow<UserProfile> = flow {
        val event = updateUserRepository.updateUsername(params.userId, params.username)
        when(event) {
            is Event.Success -> {
                val newUserProfile = event.data
                prefsDataSource.saveUserProfile(newUserProfile)
                emit(newUserProfile)
            }
            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }
}