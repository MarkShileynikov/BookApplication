package com.example.mybookapplication.presentation.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookapplication.App
import com.example.mybookapplication.data.api.NetworkClient
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import com.example.mybookapplication.data.repository.SessionRepositoryImpl
import com.example.mybookapplication.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SignInViewModel(context : Application, private val signInUseCase: SignInUseCase) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<SignInViewState>(SignInViewState.Idle)

    fun onSignInButtonClicked(email: String, password : String) {
        viewModelScope.launch {
            signInUseCase(SignInUseCase.Params(email, password))
                .onStart { viewState.value = SignInViewState.Loading }
                .catch {
                    viewState.value = SignInViewState.Failure(it.message ?: "Something went wrong.")
                }
                .collect {
                    viewState.value = SignInViewState.Success
                }
        }
    }

    companion object {
        val signInViewModelFactory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                val authApiService = NetworkClient.provideAuthApiService()
                val prefsDataSource = PrefsDataSourceImpl(context)
                val sessionRepository = SessionRepositoryImpl(context, authApiService, prefsDataSource)
                val signInUseCase = SignInUseCase(context, sessionRepository)
                return@initializer SignInViewModel(
                    context, signInUseCase
                )
            }
        }
    }

}