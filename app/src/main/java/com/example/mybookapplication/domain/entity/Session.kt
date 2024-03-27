package com.example.mybookapplication.domain.entity

data class Session(
    val token : String,
    val userProfile: UserProfile
)
