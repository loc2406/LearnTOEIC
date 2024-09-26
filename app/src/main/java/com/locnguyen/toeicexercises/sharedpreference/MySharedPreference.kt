package com.locnguyen.toeicexercises.sharedpreference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.model.Word
import java.lang.ref.WeakReference
import java.lang.reflect.Type

class MySharedPreference(private val application: Application) {

    private val sharedRef = application.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

    fun addFavoriteWord(word: Word, key: String) {
        val favoriteWords = getFavoriteWords(key).toMutableSet()
        favoriteWords.add(word)
        val favoriteWordsJson = Gson().toJson(favoriteWords)
        sharedRef.edit().putString(key, favoriteWordsJson).apply()
    }

    fun removeFavoriteWord(word: Word, key: String) {
        val favoriteWords = getFavoriteWords(key).toMutableSet()
        favoriteWords.remove(word)
        val favoriteWordsJson = Gson().toJson(favoriteWords)
        sharedRef.edit().putString(key, favoriteWordsJson).apply()
    }

    fun getFavoriteWords(key: String): Set<Word> {
        var favoriteWords: Set<Word> = emptySet()

        val favoriteWordsJson: String? = try{
            sharedRef.getString(key, "empty")
        }catch (e: ClassCastException){
            null
        }

        favoriteWordsJson?.let {
            if (it != "empty"){
                val type: Type = object: TypeToken<Set<Word>>() {}.type
                favoriteWords = Gson().fromJson(favoriteWordsJson, type)
            }
        }

        return favoriteWords
    }

    fun addFavoriteGrammar(grammar: Grammar, key: String) {
        val favoriteGrammars = getFavoriteGrammars(key).toMutableSet()
        favoriteGrammars.add(grammar)
        val favoriteGrammarsJson = Gson().toJson(favoriteGrammars)
        sharedRef.edit().putString(key, favoriteGrammarsJson).apply()
    }

    fun removeFavoriteGrammar(grammar: Grammar, key: String) {
        val favoriteGrammars = getFavoriteGrammars(key).toMutableSet()
        favoriteGrammars.remove(grammar)
        val favoriteGrammarsJson = Gson().toJson(favoriteGrammars)
        sharedRef.edit().putString(key, favoriteGrammarsJson).apply()
    }

    fun getFavoriteGrammars(key: String): Set<Grammar> {
        var favoriteGrammars: Set<Grammar> = emptySet()

        val favoriteGrammarsJson: String? = try{
            sharedRef.getString(key, "empty")
        }catch (e: ClassCastException){
            null
        }

        favoriteGrammarsJson?.let {
            if (it != "empty"){
                val type: Type = object: TypeToken<Set<Grammar>>() {}.type
                favoriteGrammars = Gson().fromJson(favoriteGrammarsJson, type)
            }
        }

        return favoriteGrammars
    }

    companion object{
        const val MY_SHARED_PREFERENCE = "MY_SHARED_PREFERENCE"
        const val FAVORITE_WORDS = "FAVORITE_WORDS"
        const val FAVORITE_GRAMMARS = "FAVORITE_GRAMMARS"

        @Volatile
        private var instance: MySharedPreference? = null

        fun getInstance(application: Application): MySharedPreference {
            return instance ?: synchronized(this) {
                instance ?: MySharedPreference(application).also {
                    instance = it
                }
            }
        }
    }
}