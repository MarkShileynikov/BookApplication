package com.example.mybookapplication.presentation.profile.settings.editprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookapplication.App
import com.example.mybookapplication.data.api.NetworkClientConfig
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import com.example.mybookapplication.data.repository.UpdateUserRepositoryImpl
import com.example.mybookapplication.domain.entity.UserProfile
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
    private val updateUserUseCase: UpdateUsernameUseCase
) : AndroidViewModel(context){
    val viewState = MutableStateFlow<ViewState<UserProfile>>(ViewState.Loading)

    fun updateUsername(id: String, usernameFromActivity: String) {
        val username = usernameFromActivity.trim().replace("\\s+".toRegex(), " ") ?: ""
        viewModelScope.launch {
            updateUserUseCase(
                UpdateUsernameUseCase.Params(id, username)
            )
                .onStart { viewState.value = ViewState.Loading }
                .catch {
                    viewState.value = ViewState.Failure(it.message ?: "Something went wrong")
                }
                .collect {userProfile ->
                    viewState.value = ViewState.Success(userProfile)
                }
        }
    }

}