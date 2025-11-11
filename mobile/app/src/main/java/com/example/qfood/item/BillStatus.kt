package com.example.qfood.item

import android.os.Parcel
import android.os.Parcelable

data class BillStatus(
    val bill_id: Int,
    val user_id: Int,
    val bill_phone: String,
    val bill_address: String,
    val bill_when: String,
    val bill_method: String,
    val bill_discount: Int,
    val bill_delivery: Int,
    val bill_total: Int,
    var bill_paid: String,
    var bill_status: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bill_id)
        parcel.writeInt(user_id)
        parcel.writeString(bill_phone)
        parcel.writeString(bill_address)
        parcel.writeString(bill_when)
        parcel.writeString(bill_method)
        parcel.writeInt(bill_discount)
        parcel.writeInt(bill_delivery)
        parcel.writeInt(bill_total)
        parcel.writeString(bill_paid)
        parcel.writeInt(bill_status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BillStatus> {
        override fun createFromParcel(parcel: Parcel): BillStatus {
            return BillStatus(parcel)
        }

        override fun newArray(size: Int): Array<BillStatus?> {
            return arrayOfNulls(size)
        }
    }

}
