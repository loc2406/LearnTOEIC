package com.locnguyen.toeicexercises.model

data class GrammarContent(
    var title: String = "",
    var content: List<GrammarSubContent> = emptyList()
)