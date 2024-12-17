package com.locnguyen.toeicexercises.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Exam(
    @SerializedName("isUnlock")
    var isUnlock: Boolean = true,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("parts")
    var parts: List<ExamPart> = emptyList(),
    @SerializedName("time")
    var time: Int = 0): Parcelable {
    constructor(parcel: Parcel) : this(
        title = parcel.readString().toString(),
        parts = parcel.createTypedArrayList(ExamPart)?: emptyList(),
        time = parcel.readInt(),
        isUnlock = parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(parts)
        parcel.writeInt(time)
        parcel.writeByte(if (isUnlock) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exam> {
        override fun createFromParcel(parcel: Parcel): Exam {
            return Exam(parcel)
        }

        override fun newArray(size: Int): Array<Exam?> {
            return arrayOfNulls(size)
        }
    }
}
