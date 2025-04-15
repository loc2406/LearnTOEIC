package com.locnguyen.toeicexercises.viewmodel

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

class ExamVM(val app: Application) : AndroidViewModel(app) {
    val dataRepo: DataRepo by lazy { DataRepo.getInstance() }
    val exams: MutableLiveData<List<Exam>> by lazy { MutableLiveData(emptyList()) }
    val exam: MutableLiveData<Exam?> by lazy { MutableLiveData() }
    val currentQuestion: MutableLiveData<Int> by lazy { MutableLiveData(0) }
    var examQuestions: ArrayList<Pair<String, Question>> = ArrayList()
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

    fun calculateExamScore() {
        for (i in examQuestions.indices) {
            val isAnswered = userAnswers[i]

            if (isAnswered != null) {
                if (examQuestions[i].second.trueAnswer == isAnswered) {
                    answerCorrect.add(examQuestions[i].second)
                }
            }
        }
    }

    fun getExamScore(): Int {
        listenCorrectAnswers = answerCorrect.filter { question -> question.media.isNotEmpty() }
        readCorrectAnswers = (answerCorrect - listenCorrectAnswers.toSet())

        var score = 0

        when (listenCorrectAnswers.size) {
            in 0..6 -> score += 5
            7 -> score += 10
            8 -> score += 15
            9 -> score += 20
            10 -> score += 25
            11 -> score += 30
            12 -> score += 35
            13 -> score += 40
            14 -> score += 45
            15 -> score += 50
            16 -> score += 55
            17 -> score += 60
            18 -> score += 65
            19 -> score += 70
            20 -> score += 75
            21 -> score += 80
            22 -> score += 85
            23 -> score += 90
            24 -> score += 95
            25 -> score += 100
            26 -> score += 110
            27 -> score += 115
            28 -> score += 120
            29 -> score += 125
            30 -> score += 130
            31 -> score += 135
            32 -> score += 140
            33 -> score += 145
            34 -> score += 150
            35 -> score += 160
            36 -> score += 165
            37 -> score += 170
            38 -> score += 175
            39 -> score += 180
            40 -> score += 185
            41 -> score += 190
            42 -> score += 195
            43 -> score += 200
            44 -> score += 210
            45 -> score += 215
            46 -> score += 220
            47 -> score += 230
            48 -> score += 240
            49 -> score += 245
            50 -> score += 250
            51 -> score += 255
            52 -> score += 260
            53 -> score += 270
            54 -> score += 275
            55 -> score += 280
            56 -> score += 290
            57 -> score += 295
            58 -> score += 300
            59 -> score += 310
            60 -> score += 315
            61 -> score += 320
            62 -> score += 325
            63 -> score += 330
            64 -> score += 340
            65 -> score += 345
            66 -> score += 350
            67 -> score += 360
            68 -> score += 365
            69 -> score += 370
            70 -> score += 380
            71 -> score += 385
            72 -> score += 390
            73 -> score += 395
            74 -> score += 400
            75 -> score += 405
            76 -> score += 410
            77 -> score += 420
            78 -> score += 425
            79 -> score += 430
            80 -> score += 440
            81 -> score += 445
            82 -> score += 450
            83 -> score += 460
            84 -> score += 465
            85 -> score += 470
            86 -> score += 475
            87 -> score += 480
            88 -> score += 485
            89 -> score += 490
            else -> score += 495 // 90 .. 100
        }

        when (readCorrectAnswers.size) {
            in 0..15 -> score += 5
            16 -> score += 10
            17 -> score += 15
            18 -> score += 20
            19 -> score += 25
            20 -> score += 30
            21 -> score += 35
            22 -> score += 40
            23 -> score += 45
            24 -> score += 50
            25 -> score += 60
            26 -> score += 65
            27 -> score += 70
            28 -> score += 80
            29 -> score += 85
            30 -> score += 90
            31 -> score += 95
            32 -> score += 100
            33 -> score += 110
            34 -> score += 115
            35 -> score += 120
            36 -> score += 125
            37 -> score += 130
            38 -> score += 140
            39 -> score += 145
            40 -> score += 150
            41 -> score += 160
            42 -> score += 165
            43 -> score += 170
            44 -> score += 175
            45 -> score += 180
            46 -> score += 190
            47 -> score += 195
            48 -> score += 200
            49 -> score += 210
            50 -> score += 215
            51 -> score += 220
            52 -> score += 225
            53 -> score += 230
            54 -> score += 235
            55 -> score += 240
            56 -> score += 250
            57 -> score += 255
            58 -> score += 260
            59 -> score += 265
            60 -> score += 270
            61 -> score += 280
            62 -> score += 285
            63 -> score += 290
            64 -> score += 300
            65 -> score += 305
            66 -> score += 310
            67 -> score += 320
            68 -> score += 325
            69 -> score += 330
            70 -> score += 335
            71 -> score += 340
            72 -> score += 350
            73 -> score += 355
            74 -> score += 360
            75 -> score += 365
            76 -> score += 370
            77 -> score += 380
            78 -> score += 385
            79 -> score += 390
            80 -> score += 395
            81 -> score += 400
            82 -> score += 405
            83 -> score += 410
            84 -> score += 415
            85 -> score += 420
            86 -> score += 425
            87 -> score += 430
            88 -> score += 435
            89 -> score += 445
            90 -> score += 450
            91 -> score += 455
            92 -> score += 465
            93 -> score += 470
            94 -> score += 480
            95 -> score += 485
            96 -> score += 490
            else -> score += 495 // 97 .. 100
        }

        return score
    }

    fun getQuestions(context: Context): List<Pair<String, Question>> {
        val allQuestions = ArrayList<Pair<String, Question>>()

        exam.value?.let {
            it.parts.forEach { part ->
                part.contents.forEach { content ->
                    val quesKey = context.getString(
                        R.string.Exam_instruction_content_regex,
                        part.title.uppercase(),
                        content.type.uppercase(),
                        content.description
                    )

                    content.questions.forEach { question ->
                        allQuestions.add(Pair(quesKey, question))
                    }
                }
            }
        }

        return allQuestions
    }
}