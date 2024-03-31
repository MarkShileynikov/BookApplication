package com.example.mybookapplication.data.api.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("objectId")
    val id : String,
    val username : String,
    val email : String,
    val avatar: String?,
)
