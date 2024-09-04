package com.locnguyen.toeicexercises.model

import com.google.gson.annotations.SerializedName

class ListJIPTObject {
    @SerializedName("Err")
    var err: Err?= null

    @SerializedName("Questions")
    var questions: List<Question>?= null

    class Err{
        @SerializedName("statusCode")
        var statusCode: Int? = null

        @SerializedName("name")
        var name: String?= null

        @SerializedName("message")
        var message: String?= null

        @SerializedName("stack")
        var stack: String?= null
    }

    class Question{
        @SerializedName("id")
        var id: Int? = null

        @SerializedName("title")
        var title: String?= null

        @SerializedName("time")
        var time: Int?= null

        @SerializedName("score")
        var score: Int?= null

        @SerializedName("pass_score")
        var passScore: Int?= null
    }
}