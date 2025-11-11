package com.example.qfood.item

import android.os.Parcel
import android.os.Parcelable

data class User(
    val user_id: Int,
    val user_name: String,
    val user_password: String,
    val user_email: String,
    val user_phone: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(user_id)
        parcel.writeString(user_name)
        parcel.writeString(user_password)
        parcel.writeString(user_email)
        parcel.writeString(user_phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}