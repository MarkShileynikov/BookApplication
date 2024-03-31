package com.example.mybookapplication.data.api.request

import com.google.gson.annotations.SerializedName

data class UserProfileRequest(
    @SerializedName("objectId")
    val id : String,
    val username : String,
    val email : String,
    val avatar: String?,
)