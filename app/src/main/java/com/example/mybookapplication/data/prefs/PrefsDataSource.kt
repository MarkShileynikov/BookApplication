package com.example.mybookapplication.data.prefs

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.mybookapplication.domain.entity.UserProfile
import javax.inject.Inject

class PrefsDataSourceImpl @Inject constructor(private val context : Context) : PrefsDataSource {
    override fun saveToken(token: String) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )
        with(prefs.edit()) {
            putString(tokenKey, token)
            apply()
        }
    }

    override fun fetchToken(): String? {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )
        return prefs.getString(tokenKey, "")
    }

    override fun saveUserProfile(userProfile: UserProfile) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        with(prefs.edit()) {
            putString(userIdKey, userProfile.userId)
            putString(emailKey, userProfile.email)
            putString(usernameKey, userProfile.username)
            putString(avatarKey, userProfile.avatar)
            apply()
        }
    }

    override fun fetchUserProfile(): UserProfile {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        return UserProfile(
            userId = prefs.getString(userIdKey, "") ?: "",
            email = prefs.getString(emailKey, "") ?: "",
            username = prefs.getString(usernameKey, "") ?: "",
            avatar = prefs.getString(avatarKey, "") ?: ""
        )
    }

    override fun deleteUserProfile() {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )
        prefs.edit().clear().apply()
    }

    companion object {
        const val sessionPrefs ="session_prefs"
        const val tokenKey = "token_key"
        const val userIdKey = "current_user_id"
        const val usernameKey = "current_username"
        const val emailKey = "current_user_email"
        const val avatarKey = "current_user_avatar"
    }
}

interface PrefsDataSource {
    fun saveToken(token: String)
    fun fetchToken() : String?
    fun saveUserProfile(userProfile: UserProfile)
    fun fetchUserProfile(): UserProfile
    fun deleteUserProfile()
}