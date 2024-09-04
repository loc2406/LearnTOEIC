package com.locnguyen.toeicexercises.model

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

data class Word(
    var id: Int?,
    var title: String?,
    var shortMean: String?,
    var listMeans: List<WordKindMean>?,
    var level: Int?,
    var pronounce: String?)

data class WordKindMean(
    var kind: String?,
    var means: List<WordMean>?
){
    companion object{
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
)

