package com.example.qfood.item

import android.os.Parcel
import android.os.Parcelable

data class BillDetail(
    val bill_id: Int,
    val food_id: Int,
    val item_qty: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bill_id)
        parcel.writeInt(food_id)
        parcel.writeInt(item_qty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BillDetail> {
        override fun createFromParcel(parcel: Parcel): BillDetail {
            return BillDetail(parcel)
        }

        override fun newArray(size: Int): Array<BillDetail?> {
            return arrayOfNulls(size)
        }
    }

}
