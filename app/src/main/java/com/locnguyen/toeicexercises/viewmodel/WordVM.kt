package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.DataManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.Example
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.repo.DataRepo
import kotlinx.coroutines.launch

class WordVM(private val app: Application) : AndroidViewModel(app) {

    private val repo: DataRepo by lazy { DataRepo(app) }

    val loadFavoriteWords: MutableLiveData<Boolean> by lazy { MutableLiveData(true) }
    val words: MutableLiveData<List<Word>> by lazy { MutableLiveData(DataManager.words.value) }
    val examples: MutableLiveData<List<Example>> by lazy { MutableLiveData(DataManager.examples.value) }
    val searchResult: MutableLiveData<List<Word>> by lazy { MutableLiveData() }
    val favWords: MutableLiveData<List<Word>> by lazy { MutableLiveData() }
    val message: MutableLiveData<String?> by lazy { MutableLiveData() }

    fun handleSearchWord(keyword: String) {
        if (keyword.trim().isNotEmpty()) {
            words.value?.let {
                searchResult.value = it.filter { word ->
                    word.title.contains(
                        keyword,
                        true
                    ) || word.shortMean.contains(keyword, true)
                }
            }
        }
    }

    fun getExEngContent(exId: Int): String {
        return examples.value?.find { ex -> ex.id == exId }?.engContent ?: ""
    }

    fun getExVieContent(exId: Int): String {
        return examples.value?.find { ex -> ex.id == exId }?.viContent ?: ""
    }

    fun addFavoriteWord(word: Word) {
        viewModelScope.launch {
            try {
                repo.addFavoriteWord(word)
                message.value = app.getString(R.string.Added_favorite_word_successful)
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }

    fun removeFavoriteWord(word: Word) {
        viewModelScope.launch {
            try {
                repo.removeFavoriteWord(word)
                message.value = app.getString(R.string.Removed_favorite_word_successful)
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }

     fun fetchFavoriteWords() {
        viewModelScope.launch {
            try{
                val result = repo.fetchFavoriteWords()
                favWords.value = result
            }catch (e: Exception) {
                message.value =e.message
            }
        }
    }
}