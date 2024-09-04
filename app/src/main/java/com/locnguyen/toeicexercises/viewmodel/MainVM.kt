package com.locnguyen.toeicexercises.viewmodel

import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.toeicexercises.model.Exam

class MainVM: ViewModel() {

    val itemExamClicked: MutableLiveData<Exam> by lazy { MutableLiveData() }
    val startLearn: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val itemTheoryClicked: MutableLiveData<String> by lazy {MutableLiveData("")}
    val itemExerciseClicked: MutableLiveData<String> by lazy {MutableLiveData("")}
}