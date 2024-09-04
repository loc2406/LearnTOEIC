package com.locnguyen.toeicexercises.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("media")
    var media: String = "",
    @SerializedName("img")
    var img: String = "",
    @SerializedName("answers")
    var answers: List<String> = emptyList(),
    @SerializedName("trueAnswer")
    var trueAnswer: String = "",
    @SerializedName("explain")
    var explain: String = "",
    @SerializedName("key")
    var key: String = ""
    ): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList()?: emptyList(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(media)
        parcel.writeString(img)
        parcel.writeStringList(answers)
        parcel.writeString(trueAnswer)
        parcel.writeString(explain)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        private var currentId: Long = 0

        fun getId() = currentId++

        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}