package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordVM(application: Application) : AndroidViewModel(application){

//    val allWords: LiveData<List<Word>>
//
//    init{
//        val wordDao = WordDatabase.instance(application).wordDao()
//        repo = WordRepo(wordDao)
//        allWords =repo.allWords
//    }
//
//    fun insert(word: Word){
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.insert(word)
//        }
//    }
}