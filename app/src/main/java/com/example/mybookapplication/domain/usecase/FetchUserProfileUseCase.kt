package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.UserRepositoryImpl
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchUserProfileUseCase(private val userRepository: UserRepositoryImpl) {
    suspend operator fun invoke() : Flow<Event<UserProfile>> = flow {
        val userProfile = userRepository.fetchUserProfile()
        emit(userProfile)
    }
}