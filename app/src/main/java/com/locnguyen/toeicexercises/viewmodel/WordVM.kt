package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.model.Example
import com.locnguyen.toeicexercises.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordVM : ViewModel(){
    val words: MutableLiveData<List<Word>> by lazy {MutableLiveData(emptyList())}
    val examples: MutableLiveData<List<Example>> by lazy {MutableLiveData(emptyList())}
    val searchResult: MutableLiveData<List<Word>> by lazy { MutableLiveData(emptyList()) }

    fun handleSearchWord(keyword: String) {
        if(keyword.trim().isNotEmpty()){
           words.value?.let{
               searchResult.value = it.filter { word -> word.title?.contains(keyword, true) == true || word.shortMean?.contains(keyword, true) == true }
           }
        }
    }

    fun getExEngContent(exId: Int): String {
        return examples.value?.find { ex -> ex.id == exId }?.engContent ?: ""
    }

    fun getExVieContent(exId: Int): String {
        return examples.value?.find { ex -> ex.id == exId }?.viContent ?: ""
    }
}