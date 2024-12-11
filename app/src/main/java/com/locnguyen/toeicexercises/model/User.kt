package com.locnguyen.toeicexercises.model

import android.util.Patterns
import java.util.regex.Pattern

data class User(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var avatar: String = "",
    var isPremium: Boolean = false,
    var favWords: List<Word> = emptyList(),
    var favGrammars: List<Grammar> = emptyList()){

    companion object{
        fun isValidName(name: String) = name.isNotBlank() && !isNotContainsSpecialCharacter(name)
        fun isValidEmail(email: String) = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(password: String) = password.isNotBlank() && password.length >= 8

        private fun isNotContainsSpecialCharacter(name: String): Boolean {
            //      \\p{L} đại diện cho tất cả các kí tự chữ cái Unicode
            //      \\p{N} đại diện cho tất cả các kí tự số Unicode
            //      \\s đại diện cho khoảng trắng
            val pattern = Pattern.compile("^[\\p{L}\\p{N}\\s]+$")
            return !pattern.matcher(name).matches()
        }
    }
}