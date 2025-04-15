package com.locnguyen.toeicexercises.viewmodel.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.repo.UserRepo
import com.locnguyen.toeicexercises.utils.Event
import kotlinx.coroutines.launch

class MainVM(val app: Application): AndroidViewModel(app) {

    val itemExamClicked: MutableLiveData<Exam> by lazy { MutableLiveData() }
    val itemTheoryClicked: MutableLiveData<Event<String>> by lazy {MutableLiveData()}
    val itemExerciseClicked: MutableLiveData<Event<String>> by lazy {MutableLiveData()}
    val wordsNoteClicked: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val grammarsNoteClicked: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}

    private val _logoutClicked: MutableLiveData<Event<Boolean>> by lazy {MutableLiveData()}
    val logoutClicked: LiveData<Event<Boolean>> by lazy {_logoutClicked}

    private val _editInfoClicked: MutableLiveData<Event<Boolean>> by lazy {MutableLiveData()}
    val editInfoClicked: LiveData<Event<Boolean>> by lazy {_editInfoClicked}

    fun logoutClicked(){
        _logoutClicked.value = Event(true)
    }

    fun logOutFb() {
        viewModelScope.launch {
            UserRepo.getInstance().signOut()
        }
    }

    fun editInfoClicked() {
        _editInfoClicked.value = Event(true)
    }
}