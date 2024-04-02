package com.example.mybookapplication.data.api.util

import android.content.Context
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.util.Event
import retrofit2.Response

suspend fun <T : Any> doCall(context : Context, call: suspend () -> Response<T>): Event<T> {
    val response = call()
    return if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            Event.Success(body)
        } else {
            Event.Failure("Unknown error")
        }
    } else {
        val errorBody = response.errorBody()?.string()
        if (!errorBody.isNullOrBlank()) {
            var apiError = errorBody.toApiError()
            when(apiError.errorCode) {
                3040 -> apiError.message = context.getString(R.string.invalid_email)
                3003 -> apiError.message = context.getString(R.string.invalid_email_or_password)
                3033 -> apiError.message = context.getString(R.string.email_already_exists)
                1155 -> apiError.message = context.getString(R.string.username_already_exists)
            }
            Event.Failure(apiError.message)
        } else {
            Event.Failure("Unknown error")
        }
    }
}