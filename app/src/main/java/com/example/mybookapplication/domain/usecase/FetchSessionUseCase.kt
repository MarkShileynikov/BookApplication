package com.example.mybookapplication.domain.usecase

import com.example.mybookapplication.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {

    suspend operator fun invoke() : Flow<String?> = sessionRepository.fetchToken()
}