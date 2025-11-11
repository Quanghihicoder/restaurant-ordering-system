package com.example.qfood.item

import android.os.Parcel
import android.os.Parcelable

data class CartFood(
    val user_id: Int,
    val food_id: Int,
    var item_qty: Int,
    val food_name: String,
    val food_price: String? = "",
    val food_discount: String? = "",
    val food_desc: String? = "",
    val food_src: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(user_id)
        parcel.writeInt(food_id)
        parcel.writeInt(item_qty)
        parcel.writeString(food_name)
        parcel.writeString(food_price)
        parcel.writeString(food_discount)
        parcel.writeString(food_desc)
        parcel.writeString(food_src)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartFood> {
        override fun createFromParcel(parcel: Parcel): CartFood {
            return CartFood(parcel)
        }

        override fun newArray(size: Int): Array<CartFood?> {
            return arrayOfNulls(size)
        }
    }

    fun calcDiscount(): Double {
        return food_discount!!.toDouble() * item_qty
    }

    fun calcPrice(): Double {
        return food_price!!.toDouble() * item_qty
    }

    fun calcTotal(): Double {
        return calcPrice() - calcDiscount()
    }
}
