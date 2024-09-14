package com.locnguyen.toeicexercises.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

data class Word(
    var id: Int?,
    var title: String?,
    var shortMean: String?,
    var listMeans: List<WordKindMean>?,
    var level: Int?,
    var pronounce: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(WordKindMean),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(shortMean)
        parcel.writeTypedList(listMeans)
        parcel.writeValue(level)
        parcel.writeString(pronounce)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }
}

data class WordKindMean(
    var kind: String?,
    var means: List<WordMean>?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(WordMean)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kind)
        parcel.writeTypedList(means)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WordKindMean> {
        override fun createFromParcel(parcel: Parcel): WordKindMean {
            return WordKindMean(parcel)
        }

        override fun newArray(size: Int): Array<WordKindMean?> {
            return arrayOfNulls(size)
        }

        fun covertFromJsonStringToList(jsonString: String): List<WordKindMean>? {
            val convertType = object:TypeToken<List<WordKindMean>>(){}.type
            val value: List<WordKindMean>? = try{
                Gson().fromJson(jsonString, convertType)
            }catch (e: JsonSyntaxException){
                null
            }

            return value
        }
    }
}

data class WordMean(
    var mean: String?,
    var examples: List<Int>?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createIntArray()?.toList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mean)
        parcel.writeIntArray(examples?.toIntArray() ?: intArrayOf())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WordMean> {
        override fun createFromParcel(parcel: Parcel): WordMean {
            return WordMean(parcel)
        }

        override fun newArray(size: Int): Array<WordMean?> {
            return arrayOfNulls(size)
        }
    }
}

