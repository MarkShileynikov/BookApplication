package com.example.mybookapplication.data.api.request

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("login")
    val email: String,
    @SerializedName("password")
    val password: String,
)