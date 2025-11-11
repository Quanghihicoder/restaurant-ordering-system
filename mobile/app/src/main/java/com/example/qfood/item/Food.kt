package com.example.qfood.item

import android.os.Parcel
import android.os.Parcelable

data class Food(
    val food_id: Int = 0,
    val food_name: String,
    val food_star: String? = "",
    val food_vote: String? = "",
    val food_price: String? = "",
    val food_discount: String? = "",
    val food_desc: String? = "",
    val food_status: String? = "",
    val food_type: String? = "",
    val food_category: String? = "",
    val food_src: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(food_id)
        parcel.writeString(food_name)
        parcel.writeString(food_star)
        parcel.writeString(food_vote)
        parcel.writeString(food_price)
        parcel.writeString(food_discount)
        parcel.writeString(food_desc)
        parcel.writeString(food_status)
        parcel.writeString(food_type)
        parcel.writeString(food_category)
        parcel.writeString(food_src)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }

    fun calcPrice(): Double {
        return food_price!!.toDouble() - food_discount!!.toDouble()
    }
}
