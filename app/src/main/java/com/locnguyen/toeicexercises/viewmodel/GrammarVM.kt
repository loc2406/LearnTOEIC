package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference

class GrammarVM(application: Application): AndroidViewModel(application) {

    private val shareRef: MySharedPreference by lazy { MySharedPreference(application) }
    val loadFavoriteGrammars: MutableLiveData<Boolean> by lazy { MutableLiveData(true) }
    val grammars: MutableLiveData<List<Grammar>> by lazy {MutableLiveData(emptyList())}
    val currentLesson: MutableLiveData<Int> by lazy {MutableLiveData()}

    fun addFavoriteGrammar(grammar: Grammar) {
        shareRef.addFavoriteGrammar(grammar, MySharedPreference.FAVORITE_GRAMMARS)
    }

    fun removeFavoriteGrammar(grammar: Grammar) {
        shareRef.removeFavoriteGrammar(grammar, MySharedPreference.FAVORITE_GRAMMARS)
    }

    fun getFavoriteGrammars(): Set<Grammar> {
        return shareRef.getFavoriteGrammars(MySharedPreference.FAVORITE_GRAMMARS)
    }
}