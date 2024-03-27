package com.example.mybookapplication.domain.entity

data class UserProfile(
    val userId: String,
    val email: String,
    val username: String,
    val avatar: String?
)

fun UserProfile.isValid(): Boolean = userId.isBlank()
