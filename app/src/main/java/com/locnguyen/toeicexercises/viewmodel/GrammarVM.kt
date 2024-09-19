package com.locnguyen.toeicexercises.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.utils.TheoryDB

class GrammarVM: ViewModel() {
    val grammars: MutableLiveData<List<Grammar>> by lazy {MutableLiveData(emptyList())}

    val currentLesson: MutableLiveData<Int> by lazy {MutableLiveData()}
}