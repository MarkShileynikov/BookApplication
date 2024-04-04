package com.example.mybookapplication.presentation.profile.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.usecase.FetchUserProfileUseCase
import com.example.mybookapplication.domain.usecase.SignOutUseCase
import com.example.mybookapplication.domain.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(context : Application, private val signOutUseCase: SignOutUseCase, private val fetchUserProfileUseCase: FetchUserProfileUseCase) : AndroidViewModel(context) {
    private val _userProfile = MutableStateFlow<Event<UserProfile>>(Event.Failure("No profile found"))

    init {
        fetchUserProfile()
    }
    val userProfile: StateFlow<Event<UserProfile>> get() = _userProfile

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            fetchUserProfileUseCase()
                .catch {
                    _userProfile.value = Event.Failure("No profile found")
                }
                .collect { userProfile ->
                    _userProfile.value = userProfile
                }
        }
    }
}