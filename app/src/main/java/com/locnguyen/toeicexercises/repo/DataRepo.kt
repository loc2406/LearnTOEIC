package com.locnguyen.toeicexercises.repo

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.Exam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DataRepo(private val context: Context) {

    private val fbDbInstance: FirebaseDatabase by lazy { Firebase.database }
    private val examsDbRef: DatabaseReference by lazy { fbDbInstance.getReference("exams") }

    private suspend fun getAllExamsInFb(): DataSnapshot? = examsDbRef.get().await()

    suspend fun allExams(): List<Exam> {
        return withContext(Dispatchers.IO) {
            val result: ArrayList<Exam> = ArrayList()
            val examsSnapshot: DataSnapshot? = try {
                getAllExamsInFb()
            } catch (e: Exception) {
                throw Exception(context.getString(R.string.Something_went_wrong_please_try_again))
            }

            examsSnapshot?.let { snapShots ->
                if (snapShots.exists()) {
                    snapShots.children.forEach { examSnapshot ->
                        val exam = examSnapshot.getValue(Exam::class.java)

                        exam?.let { result.add(it) }
                    }
                }
            }

            result
        }
    }
}