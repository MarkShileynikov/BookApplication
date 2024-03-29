package com.example.mybookapplication.presentation.profile.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookapplication.App
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import com.example.mybookapplication.data.repository.SessionRepositoryImpl
import com.example.mybookapplication.data.repository.UserRepositoryImpl
import com.example.mybookapplication.domain.usecase.SignOutUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(context : Application, private val signOutUseCase: SignOutUseCase) : AndroidViewModel(context) {

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    companion object {
        val settingsViewModelFactory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val prefsDataSource = PrefsDataSourceImpl(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App)
                val userRepository = UserRepositoryImpl(prefsDataSource)
                val signOutUseCase = SignOutUseCase(userRepository)
                return@initializer SettingsViewModel(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App,
                    signOutUseCase
                )

            }
        }
    }
}