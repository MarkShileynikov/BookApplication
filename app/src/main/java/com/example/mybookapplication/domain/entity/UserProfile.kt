package com.example.mybookapplication.domain.entity

import android.os.Parcel
import android.os.Parcelable

data class UserProfile(
    val userId: String,
    val email: String,
    val username: String,
    val avatar: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfile> {
        override fun createFromParcel(parcel: Parcel): UserProfile {
            return UserProfile(parcel)
        }

        override fun newArray(size: Int): Array<UserProfile?> {
            return arrayOfNulls(size)
        }
    }
}

fun UserProfile.isValid(): Boolean = userId.isNotBlank()
