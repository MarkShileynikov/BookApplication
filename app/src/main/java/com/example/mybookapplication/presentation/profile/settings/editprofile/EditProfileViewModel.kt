package com.example.mybookapplication.presentation.profile.settings.editprofile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.usecase.UpdateAvatarUseCase
import com.example.mybookapplication.domain.usecase.UpdateUsernameUseCase
import com.example.mybookapplication.presentation.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    context: Application,
    private val updateUserUseCase: UpdateUsernameUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase
) : AndroidViewModel(context){
    val updateUsernameViewState = MutableStateFlow<ViewState<UserProfile>>(ViewState.Loading)
    val updateAvatarViewState = MutableStateFlow<ViewState<UserProfile>>(ViewState.Loading)


    fun updateUsername(id: String, usernameFromActivity: String) {
        val username = usernameFromActivity.trim().replace("\\s+".toRegex(), " ") ?: ""
        viewModelScope.launch {
            updateUserUseCase(
                UpdateUsernameUseCase.Params(id, username)
            )
                .onStart { updateUsernameViewState.value = ViewState.Loading }
                .catch {
                    updateUsernameViewState.value = ViewState.Failure(it.message ?: "Something went wrong")
                }
                .collect {userProfile ->
                    updateUsernameViewState.value = ViewState.Success(userProfile)
                }
        }
    }

    fun updateAvatar(uri: Uri, userId: String) {
        viewModelScope.launch {
            updateAvatarUseCase(uri, userId)
                .onStart { updateAvatarViewState.value = ViewState.Loading }
                .catch { updateAvatarViewState.value = ViewState.Failure(it.message ?: "Something went wrong") }
                .collect {userProfile ->
                    updateAvatarViewState.value = ViewState.Success(userProfile)
                }
        }
    }

}