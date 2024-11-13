package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.DataManager
import com.locnguyen.toeicexercises.database.TheoryDb
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import kotlinx.coroutines.launch

class GrammarVM(private val app: Application): AndroidViewModel(app) {

    private val shareRef: MySharedPreference by lazy { MySharedPreference(app) }
    val loadFavoriteGrammars: MutableLiveData<Boolean> by lazy { MutableLiveData(true) }
    val grammars: MutableLiveData<List<Grammar>> by lazy {MutableLiveData(DataManager.grammars.value)}
    val currentLesson: MutableLiveData<Int> by lazy {MutableLiveData()}
    val err: MutableLiveData<String?> by lazy {MutableLiveData()}

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