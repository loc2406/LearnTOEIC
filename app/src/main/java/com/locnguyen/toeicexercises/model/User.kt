package com.locnguyen.toeicexercises.model

data class User(
    var email: String = "",
    var name: String = "",
    var password: String = "",
    var avatar: String = "",
    var isPremium: Boolean = false,
    var favWords: List<Word> = emptyList(),
    var favGrammars: List<Grammar> = emptyList())