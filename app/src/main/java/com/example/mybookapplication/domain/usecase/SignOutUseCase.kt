package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.domain.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke() {
        userRepository.deleteUserProfile()
    }
}