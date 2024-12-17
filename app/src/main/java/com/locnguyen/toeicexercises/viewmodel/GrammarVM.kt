package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.DataManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.database.TheoryDb
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.repo.DataRepo
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.utils.Event
import kotlinx.coroutines.launch

class GrammarVM(private val app: Application): AndroidViewModel(app) {

    private val repo: DataRepo by lazy { DataRepo.getInstance() }
    val isNeedLoaded: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData(Event(true)) }
    val grammars: MutableLiveData<List<Grammar>> by lazy {MutableLiveData(DataManager.grammars.value)}
    val favGrammars: MutableLiveData<List<Grammar>> by lazy { MutableLiveData() }
    val currentLesson: MutableLiveData<Int> by lazy {MutableLiveData()}
    val message: MutableLiveData<Event<String>> by lazy {MutableLiveData()}

    fun addFavoriteGrammar(grammar: Grammar) {
        viewModelScope.launch {
            try {
                repo.addFavoriteGrammar(grammar)
                message.value = Event(app.getString(R.string.Add_favorite_grammar_successful))
            } catch (e: Exception) {
                message.value = Event(app.getString(R.string.Add_favorite_grammar_failed))
            }
        }
    }

    fun removeFavoriteGrammar(grammar: Grammar) {
        viewModelScope.launch {
            try {
                repo.removeFavoriteGrammar(grammar)
                message.value = Event(app.getString(R.string.Remove_favorite_word_successful))
            } catch (e: Exception) {
                message.value = Event(app.getString(R.string.Remove_favorite_word_failed))
            }
        }
    }

    fun fetchFavoriteGrammars() {
        viewModelScope.launch {
            try {
                val result = repo.fetchFavoriteGrammars()
                favGrammars.postValue(result)
            } catch (e: Exception) {
                message.value = Event(app.getString(R.string.Get_favorite_grammar_failed))
            }
        }
    }
}