package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import android.view.View
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
    val searchFilteredList: MutableLiveData<List<Word>> by lazy { MutableLiveData(words.value) }
    val levelFilteredList: MutableLiveData<List<Word>> by lazy { MutableLiveData(words.value) }
    val favWords: MutableLiveData<List<Word>> by lazy { MutableLiveData() }
    val message: MutableLiveData<String?> by lazy { MutableLiveData() }
    val level: MutableLiveData<Int> by lazy { MutableLiveData(1) }

    fun handleSearchChange(keyword: String) {
        if (keyword.trim().isNotEmpty()) {
            words.value?.let {
                searchFilteredList.value = it.filter { word ->
                    word.title.contains(keyword, true) || word.shortMean.contains(keyword, true)
                }
            }
        }
    }

    fun handleLevelChange(view: View, isChecked: Boolean) {
        when{
            view.id == R.id.btn_level_1 && isChecked -> {
                levelFilteredList.value = words.value?.filter { word -> word.level == 1 }
                level.value = 1
            }

            view.id ==R.id.btn_level_2 && isChecked-> {
                levelFilteredList.value = words.value?.filter { word -> word.level == 2 }
                level.value = 2
            }

            view.id == R.id.btn_level_3 && isChecked-> {
                levelFilteredList.value = words.value?.filter { word -> word.level == 3 }
                level.value = 3
            }

            view.id ==R.id.btn_level_4 && isChecked-> {
                levelFilteredList.value = words.value?.filter { word -> word.level == 4 }
                level.value = 4
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
            try {
                val result = repo.fetchFavoriteWords()
                favWords.value = result
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }
}