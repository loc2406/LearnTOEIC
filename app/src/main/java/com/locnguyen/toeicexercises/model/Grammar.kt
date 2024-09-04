package com.locnguyen.toeicexercises.model

data class Grammar(
    var id: Long = getId(),
    var title: String = "",
    var key: String = "",
    var tag: List<String> = emptyList(),
    var related: List<Long> = emptyList(),
    var level: Int = 0,
    var content: List<GrammarContent>
){
    companion object{
        private var currentId: Long = 0

        fun getId() = currentId++
    }
}