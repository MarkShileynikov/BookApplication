package com.example.mybookapplication.domain.util

sealed class Event<out T : Any> {
    data class Success<out T : Any>(val data: T) : Event<T>()
    data class Failure(var exception: String) : Event<Nothing>()
}