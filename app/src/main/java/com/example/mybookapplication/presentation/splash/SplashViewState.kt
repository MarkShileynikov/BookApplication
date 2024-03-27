package com.example.mybookapplication.presentation.splash

sealed interface SplashViewState {
    data object Success : SplashViewState
    data object Failure : SplashViewState
    data object Loading : SplashViewState
}