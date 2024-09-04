package com.locnguyen.toeicexercises.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ExamPart(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("contents")
    var contents: List<ExamPartContent> = emptyList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.createTypedArrayList(ExamPartContent)?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(contents)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExamPart> {
        override fun createFromParcel(parcel: Parcel): ExamPart {
            return ExamPart(parcel)
        }

        override fun newArray(size: Int): Array<ExamPart?> {
            return arrayOfNulls(size)
        }
    }
}