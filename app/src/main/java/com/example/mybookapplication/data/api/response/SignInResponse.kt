package com.example.mybookapplication.data.api.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("objectId")
    val id: String,
    @SerializedName("user-token")
    val token: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar")
    val avatar: String,
)
