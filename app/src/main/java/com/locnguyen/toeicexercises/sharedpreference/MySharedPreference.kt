package com.locnguyen.toeicexercises.sharedpreference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.model.User
import com.locnguyen.toeicexercises.model.Word
import java.lang.ref.WeakReference
import java.lang.reflect.Type

class MySharedPreference(private val application: Application) {

    private val sharedRef =
        application.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

    fun addFavoriteWord(word: Word, key: String = FAVORITE_WORDS) {
        val favoriteWords = getFavoriteWords(key).toMutableSet()
        favoriteWords.add(word)
        val favoriteWordsJson = Gson().toJson(favoriteWords)
        sharedRef.edit().putString(key, favoriteWordsJson).apply()
    }

    fun removeFavoriteWord(word: Word, key: String = FAVORITE_WORDS) {
        val favoriteWords = getFavoriteWords(key).toMutableSet()
        favoriteWords.remove(word)
        val favoriteWordsJson = Gson().toJson(favoriteWords)
        sharedRef.edit().putString(key, favoriteWordsJson).apply()
    }

    fun getFavoriteWords(key: String = FAVORITE_WORDS): Set<Word> {
        var favoriteWords: Set<Word> = emptySet()

        val favoriteWordsJson: String? = try {
            sharedRef.getString(key, "empty")
        } catch (e: ClassCastException) {
            null
        }

        favoriteWordsJson?.let {
            if (it != "empty") {
                val type: Type = object : TypeToken<Set<Word>>() {}.type
                favoriteWords = Gson().fromJson(it, type)
            }
        }

        return favoriteWords
    }

    fun addFavoriteGrammar(grammar: Grammar, key: String = FAVORITE_GRAMMARS) {
        val favoriteGrammars = getFavoriteGrammars(key).toMutableSet()
        favoriteGrammars.add(grammar)
        val favoriteGrammarsJson = Gson().toJson(favoriteGrammars)
        sharedRef.edit().putString(key, favoriteGrammarsJson).apply()
    }

    fun removeFavoriteGrammar(grammar: Grammar, key: String = FAVORITE_GRAMMARS) {
        val favoriteGrammars = getFavoriteGrammars(key).toMutableSet()
        favoriteGrammars.remove(grammar)
        val favoriteGrammarsJson = Gson().toJson(favoriteGrammars)
        sharedRef.edit().putString(key, favoriteGrammarsJson).apply()
    }

    fun getFavoriteGrammars(key: String = FAVORITE_GRAMMARS): Set<Grammar> {
        var favoriteGrammars: Set<Grammar> = emptySet()

        val favoriteGrammarsJson: String? = try {
            sharedRef.getString(key, "empty")
        } catch (e: ClassCastException) {
            null
        }

        favoriteGrammarsJson?.let {
            if (it != "empty") {
                val type: Type = object : TypeToken<Set<Grammar>>() {}.type
                favoriteGrammars = Gson().fromJson(it, type)
            }
        }

        return favoriteGrammars
    }

    fun setUser(user: User?, key: String = USER) {
        val userJson = Gson().toJson(user)
        sharedRef.edit().putString(key, userJson).apply()
    }

    fun getUser(key: String = USER): User? {
        var user: User? = null
        val userJson: String? = try {
            sharedRef.getString(key, "empty")
        } catch (e: ClassCastException) {
            null
        }

        userJson?.let {
            if (it != "empty") {
                val type: Type = object : TypeToken<User>() {}.type
                user = Gson().fromJson(it, type)
            }
        }

        return user
    }

    fun setIsLoggedIn(isLogged: Boolean, key: String = IS_LOGGED_IN) {
        sharedRef.edit().putBoolean(key, isLogged).apply()
    }

    fun getIsLoggedIn(key: String = IS_LOGGED_IN): Boolean {
        return sharedRef.getBoolean(key, false)
    }

    companion object {
        const val MY_SHARED_PREFERENCE = "MY_SHARED_PREFERENCE"
        const val FAVORITE_WORDS = "FAVORITE_WORDS"
        const val FAVORITE_GRAMMARS = "FAVORITE_GRAMMARS"
        const val USER = "USER"
        const val IS_LOGGED_IN = "IS_LOGGED_IN"

        @Volatile
        private var instance: MySharedPreference? = null

        fun getInstance(application: Application): MySharedPreference {
            return instance ?: synchronized(this) {
                instance = MySharedPreference(application)
                instance!!
            }
        }
    }
}