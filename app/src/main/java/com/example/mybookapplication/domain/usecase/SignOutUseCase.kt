package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.UserRepositoryImpl
import com.example.mybookapplication.domain.repository.UserRepository

class SignOutUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke() {
        userRepository.deleteUserProfile()
    }
}