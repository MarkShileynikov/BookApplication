package com.example.mybookapplication.domain.entity

import android.os.Parcel
import android.os.Parcelable

data class Book(
    val id : String,
    val title: String,
    val author: String,
    val genre: String,
    val description: String,
    val releaseYear: Int,
    val ageLimit: Int,
    val cover: String,
    val pages: Int,
    val url: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(genre)
        parcel.writeString(description)
        parcel.writeInt(releaseYear)
        parcel.writeInt(ageLimit)
        parcel.writeString(cover)
        parcel.writeInt(pages)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}


