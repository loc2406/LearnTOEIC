package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.toeicexercises.database.TheoryDB
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference

class GrammarVM(private val app: Application): AndroidViewModel(app) {

    private val shareRef: MySharedPreference by lazy { MySharedPreference(app) }
    private val theoryDb: TheoryDB by lazy {TheoryDB(app)}
    val loadFavoriteGrammars: MutableLiveData<Boolean> by lazy { MutableLiveData(true) }
    val grammars: MutableLiveData<List<Grammar>> by lazy {MutableLiveData(theoryDb.getListGrammars())}
    val currentLesson: MutableLiveData<Int> by lazy {MutableLiveData()}

    fun addFavoriteGrammar(grammar: Grammar) {
        shareRef.addFavoriteGrammar(grammar)
    }

    fun removeFavoriteGrammar(grammar: Grammar) {
        shareRef.removeFavoriteGrammar(grammar)
    }

    fun getFavoriteGrammars(): Set<Grammar> {
        return shareRef.getFavoriteGrammars()
    }
}