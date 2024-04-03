package com.example.mybookapplication.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.usecase.FetchUserProfileUseCase
import com.example.mybookapplication.domain.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    context : Application,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase
) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<Event<UserProfile>>(Event.Failure("No profile found"))

    fun fetchUserProfile() {
        viewModelScope.launch {
            fetchUserProfileUseCase()
                .catch {
                    viewState.value = Event.Failure("No profile found")
                }
                .collect { userProfile ->
                    viewState.value = userProfile
                }
        }
    }
}