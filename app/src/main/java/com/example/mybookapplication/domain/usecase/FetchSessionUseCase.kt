package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.data.repository.SessionRepositoryImpl
import com.example.mybookapplication.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class FetchSessionUseCase(private val sessionRepository: SessionRepositoryImpl) {
    suspend operator fun invoke() : Flow<String?> = sessionRepository.fetchToken()
}