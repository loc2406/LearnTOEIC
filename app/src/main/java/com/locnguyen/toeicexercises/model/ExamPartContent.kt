package com.locnguyen.toeicexercises.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ExamPartContent(
    @SerializedName("type")
    var type: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("questions")
    var questions: List<Question> = emptyList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(Question)?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeTypedList(questions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExamPartContent> {
        override fun createFromParcel(parcel: Parcel): ExamPartContent {
            return ExamPartContent(parcel)
        }

        override fun newArray(size: Int): Array<ExamPartContent?> {
            return arrayOfNulls(size)
        }
    }
}
