package com.locnguyen.toeicexercises.viewmodel.main.practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.toeicexercises.utils.Event

class PracticeVM: ViewModel() {

    private val _wordClicked: MutableLiveData<Event<Boolean>> by lazy {MutableLiveData()}
    val wordClicked: LiveData<Event<Boolean>> by lazy {_wordClicked}

    private val _grammarClicked: MutableLiveData<Event<Boolean>> by lazy {MutableLiveData()}
    val grammarClicked: LiveData<Event<Boolean>> by lazy {_grammarClicked}

    private val _selectAnswerClicked: MutableLiveData<Event<Boolean>> by lazy {MutableLiveData()}
    val selectAnswerClicked: LiveData<Event<Boolean>> by lazy {_selectAnswerClicked}

    private val _readParagraphClicked: MutableLiveData<Event<Boolean>> by lazy {MutableLiveData()}
    val readParagraphClicked: LiveData<Event<Boolean>> by lazy {_readParagraphClicked}

    fun wordClicked(){
        _wordClicked.value = Event(true)
    }

    fun grammarClicked(){
        _grammarClicked.value = Event(true)
    }

    fun selectAnswerClicked(){
        _selectAnswerClicked.value = Event(true)
    }

    fun readParagraphClicked(){
        _readParagraphClicked.value = Event(true)
    }
}