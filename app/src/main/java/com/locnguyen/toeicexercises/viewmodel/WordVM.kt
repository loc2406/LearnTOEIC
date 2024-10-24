package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.database.TheoryDB
import com.locnguyen.toeicexercises.model.Example
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.repo.UserRepo
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WordVM(private val app: Application) : AndroidViewModel(app) {

    private val sharedRef: MySharedPreference by lazy { MySharedPreference(app) }
    private val theoryDb: TheoryDB by lazy { TheoryDB(app) }
    private val userRepo: UserRepo by lazy { UserRepo(app) }

    val loadFavoriteWords: MutableLiveData<Boolean> by lazy { MutableLiveData(true) }
    val words: MutableLiveData<List<Word>> by lazy { MutableLiveData(theoryDb.getListWord()) }
    val examples: MutableLiveData<List<Example>> by lazy { MutableLiveData(theoryDb.getListExamples()) }
    val searchResult: MutableLiveData<List<Word>> by lazy { MutableLiveData(emptyList()) }
    val favWords: MutableLiveData<List<Word>> by lazy { MutableLiveData() }

    val message: MutableLiveData<String?> by lazy { MutableLiveData() }

    fun handleSearchWord(keyword: String) {
        if (keyword.trim().isNotEmpty()) {
            words.value?.let {
                searchResult.value = it.filter { word ->
                    word.title?.contains(
                        keyword,
                        true
                    ) == true || word.shortMean?.contains(keyword, true) == true
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
            val isAdded = try {
                userRepo.addFavoriteWord(word)
            } catch (e: Exception) {
                message.postValue(e.message)
                false
            }

            if (isAdded) message.postValue(app.getString(R.string.Added_favorite_word_successful))
        }
    }

    fun removeFavoriteWord(word: Word) {
        viewModelScope.launch {
            val isRemoved = try {
                userRepo.removeFavoriteWord(word)
            } catch (e: Exception) {
                message.postValue(e.message)
                false
            }

            if (isRemoved) message.postValue(app.getString(R.string.Removed_favorite_word_successful))
        }
    }

    fun fetchFavoriteWords() {
        viewModelScope.launch {
            val result = try {
                userRepo.fetchFavoriteWords()
            } catch (e: Exception) {
                message.postValue(e.message)
                null
            }

            favWords.postValue(result)
        }
    }
}