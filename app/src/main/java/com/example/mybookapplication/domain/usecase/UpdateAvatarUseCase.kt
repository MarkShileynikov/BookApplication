package com.example.mybookapplication.domain.usecase

import android.net.Uri
import com.example.mybookapplication.data.prefs.PrefsDataSource
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.repository.UpdateUserRepository
import com.example.mybookapplication.domain.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateAvatarUseCase @Inject constructor(
    private val updateUserRepository: UpdateUserRepository,
    private val prefsDataSource: PrefsDataSource
) {
    suspend operator fun invoke(uri: Uri, userId: String) : Flow<UserProfile> = flow {
        val event = updateUserRepository.updateAvatar(uri, userId)
        when(event) {
            is Event.Success -> {
                val userProfile = event.data
                prefsDataSource.saveUserProfile(userProfile)
                emit(userProfile)
            }
            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }
}