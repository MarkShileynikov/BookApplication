package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.UserRepositoryImpl

class SignOutUseCase(private val userRepository: UserRepositoryImpl) {
    suspend operator fun invoke() {
        userRepository.deleteUserProfile()
    }
}