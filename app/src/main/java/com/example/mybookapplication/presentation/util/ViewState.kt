package com.example.mybookapplication.presentation.util

sealed interface ViewState<out T> {
    data class Success<T>(val data: T) : ViewState<T>
    data class Failure(val message: String) : ViewState<Nothing>
    data object Loading : ViewState<Nothing>
}