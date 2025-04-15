package com.locnguyen.toeicexercises.viewmodel.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.model.ExamPart
import com.locnguyen.toeicexercises.model.Question
import com.locnguyen.toeicexercises.repo.DataRepo
import com.locnguyen.toeicexercises.repo.UserRepo
import com.locnguyen.toeicexercises.utils.toastMessage
import kotlinx.coroutines.launch

class ExerciseVM(val app: Application) : AndroidViewModel(app) {
    val dataRepo: DataRepo by lazy { DataRepo.getInstance() }
    val exams: MutableLiveData<List<Exam>> by lazy { MutableLiveData(emptyList()) }
    val currentQuestion: MutableLiveData<Int> by lazy { MutableLiveData(0) }
    var exerciseQuestions: ArrayList<Pair<String, Question>> = ArrayList()
    var userAnswers: HashMap<Int, String> = HashMap()
    val answerCorrect: ArrayList<Question> by lazy { ArrayList() }
    var listenCorrectAnswers: List<Question> = emptyList()
    var readCorrectAnswers: List<Question> = emptyList()

    fun getAllExamInFb() {
        viewModelScope.launch {
            val result: List<Exam>? = try{
                dataRepo.allExams(app)
            }catch (e: Exception){
                null
            }
            exams.postValue(result)
        }
    }

    fun getQuestions(context: Context, displayPart: String): List<Pair<String, Question>> {
        val allQuestions = ArrayList<Pair<String, Question>>()

        exams.value?.get(0)?.let {
            when(displayPart){
                "Chọn đáp án" -> {
                    it.parts[0].contents.forEach { content ->
                        val quesKey = context.getString(
                            R.string.Exam_instruction_content_regex,
                            it.parts[0].title.uppercase(),
                            content.type.uppercase(),
                            content.description
                        )

                        content.questions.forEach { question ->
                            allQuestions.add(Pair(quesKey, question))
                        }
                    }

                    allQuestions.shuffle()
                }

                "Đọc hiểu" -> {
                    it.parts[1].contents.forEach { content ->
                        val quesKey = context.getString(
                            R.string.Exam_instruction_content_regex,
                            it.parts[1].title.uppercase(),
                            content.type.uppercase(),
                            content.description
                        )

                        content.questions.forEach { question ->
                            allQuestions.add(Pair(quesKey, question))
                        }
                    }
                    allQuestions.shuffle()
                }

                else -> {}
            }
        }

        return allQuestions
    }
}