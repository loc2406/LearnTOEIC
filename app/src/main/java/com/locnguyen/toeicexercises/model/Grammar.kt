package com.locnguyen.toeicexercises.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

data class Grammar(
    var id: Int = -1,
    var title: String = "",
    var tags: List<String> = emptyList(),
    var level: Int = 1,
    var contents: List<GrammarSubContent> = emptyList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.createStringArrayList()?.toList() ?: emptyList(),
        parcel.readInt(),
        parcel.createTypedArrayList(GrammarSubContent)?.toList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeStringList(tags)
        parcel.writeInt(level)
        parcel.writeTypedList(contents)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Grammar> {
        override fun createFromParcel(parcel: Parcel): Grammar {
            return Grammar(parcel)
        }

        override fun newArray(size: Int): Array<Grammar?> {
            return arrayOfNulls(size)
        }

        fun covertFromJsonStringToList(jsonString: String): List<String>? {
            val convertType = object:TypeToken<List<String>>(){}.type
            val value: List<String>? = try{
                Gson().fromJson(jsonString, convertType)
            }catch (e: JsonSyntaxException){
                null
            }

            return value
        }
    }
}

data class GrammarSubContent(
    @SerializedName("sub_title") // vì trong file db tên cột là sub_title
    var subTitle: String = "",
    @SerializedName("content")
    var subContents: List<GrammarContent> = emptyList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.createTypedArrayList(GrammarContent)?.toList() ?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subTitle)
        parcel.writeTypedList(subContents)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GrammarSubContent> {
        override fun createFromParcel(parcel: Parcel): GrammarSubContent {
            return GrammarSubContent(parcel)
        }

        override fun newArray(size: Int): Array<GrammarSubContent?> {
            return arrayOfNulls(size)
        }

        fun covertFromJsonStringToList(jsonString: String): List<GrammarSubContent>? {
            val convertType = object: TypeToken<List<GrammarSubContent>>(){}.type
            val value: List<GrammarSubContent>? = try{
                Gson().fromJson(jsonString, convertType)
            }catch (e: JsonSyntaxException){
                null
            }

            return value
        }
    }
}

data class GrammarContent(
    @SerializedName("c")
    var content: String = "",
    @SerializedName("f")
    var formulas: List<String> = emptyList(),
    @SerializedName("e")
    var examples: List<GrammarEx> = emptyList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.createStringArrayList()?.toList() ?: emptyList(),
        parcel.createTypedArrayList(GrammarEx)?.toList() ?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeStringList(formulas)
        parcel.writeTypedList(examples)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GrammarContent> {
        override fun createFromParcel(parcel: Parcel): GrammarContent {
            return GrammarContent(parcel)
        }

        override fun newArray(size: Int): Array<GrammarContent?> {
            return arrayOfNulls(size)
        }
    }
}

data class GrammarEx(
    @SerializedName("e")
    var example: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(example)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GrammarEx> {
        override fun createFromParcel(parcel: Parcel): GrammarEx {
            return GrammarEx(parcel)
        }

        override fun newArray(size: Int): Array<GrammarEx?> {
            return arrayOfNulls(size)
        }
    }
}