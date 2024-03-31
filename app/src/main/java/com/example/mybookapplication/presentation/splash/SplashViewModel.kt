package com.example.mybookapplication.presentation.splash

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
import com.example.mybookapplication.domain.usecase.FetchSessionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(context : Application, private val fetchSessionUseCase: FetchSessionUseCase) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<SplashViewState>(SplashViewState.Loading)

    init {
        verifySession()
    }
    private fun verifySession() {
        viewModelScope.launch {
            fetchSessionUseCase()
                .onStart { delay(1000) }
                .catch {
                    viewState.value =
                        SplashViewState.Failure
                }
                .collect {token ->
                    viewState.value = if (token.isNullOrEmpty()) {
                        SplashViewState.Failure
                    }
                    else {
                        SplashViewState.Success
                    }

                }
        }
    }
    companion object {
        val splashViewModelFactory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                val authApiService = NetworkClient.provideAuthApiService()
                val prefsDataSource = PrefsDataSourceImpl(context)
                val sessionRepository = SessionRepositoryImpl(context, authApiService, prefsDataSource)
                val fetchSessionUseCase = FetchSessionUseCase(sessionRepository)
                return@initializer SplashViewModel(
                    context, fetchSessionUseCase
                )
            }
        }
    }

}