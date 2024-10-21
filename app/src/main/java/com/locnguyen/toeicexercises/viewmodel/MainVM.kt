package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.model.User
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference

class MainVM(val app: Application): AndroidViewModel(app) {

    val user: MutableLiveData<User> by lazy { MutableLiveData(MySharedPreference.getInstance(app).getUser())}
    val itemExamClicked: MutableLiveData<Exam> by lazy { MutableLiveData() }
    val itemTheoryClicked: MutableLiveData<String> by lazy {MutableLiveData()}
    val itemExerciseClicked: MutableLiveData<String> by lazy {MutableLiveData()}
    val wordsNoteClicked: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val grammarsNoteClicked: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}

    val logoutClicked: MutableLiveData<Boolean> by lazy {MutableLiveData()}

    fun logoutClicked(){
        logoutClicked.value = true
    }
}