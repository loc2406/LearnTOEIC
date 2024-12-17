package com.locnguyen.toeicexercises.repo

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.FileReader
import java.io.IOException

class DataRepo() {

    companion object {
        @Volatile
        private var instance: DataRepo? = null
        private var isInitialized = false

        fun init() {
            if (!isInitialized){
                instance = DataRepo().also { instance = it }
                isInitialized = true
            }
        }

        fun getInstance(): DataRepo {
            return instance ?: synchronized(this) {
                init()
                instance!!
            }
        }
    }

    private val db: FirebaseDatabase by lazy { Firebase.database }
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val examsDbRef: DatabaseReference by lazy { db.getReference("exams") }
    private val usersDbRef: DatabaseReference by lazy { db.getReference("users") }

//    private suspend fun getAllExamsInFb(): DataSnapshot? = examsDbRef.get().await()

    suspend fun allExams(context: Context): List<Exam> {
        return withContext(Dispatchers.IO) {
//            val result: MutableList<Exam> = mutableListOf()
//            val examsSnapshot: DataSnapshot? = try {
//                getAllExamsInFb()
//            } catch (e: Exception) {
//                throw Exception(context.getString(R.string.Something_went_wrong_please_try_again))
//            }
//
//            examsSnapshot?.let { snapShots ->
//                if (snapShots.exists()) {
//                    snapShots.children.forEach { examSnapshot ->
//                        val exam = examSnapshot.getValue(Exam::class.java)
//
//                        exam?.let { result.add(it) }
//                    }
//                }
//            }
//            result

            val inputStream = context.assets.open("exams.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            Gson().fromJson(jsonString, Array<Exam>::class.java).toList()
        }
    }

    private fun getUserRef(): DatabaseReference? {
        val userRef: String? = auth.currentUser?.email?.substringBefore('@')
        return if (userRef != null) {
            usersDbRef.child(userRef)
        } else {
            null
        }
    }

    suspend fun addFavoriteWord(newWord: Word) {
        return withContext(Dispatchers.IO) {
            val currentSet: MutableSet<Word> = fetchFavoriteWords().toMutableSet()

            if (currentSet.contains(newWord)) {
                throw Exception("Từ vựng đã tồn tại!")
            }

            currentSet.add(newWord)
            getUserRef()?.child("favWords")?.setValue(currentSet.toList())?.await()
        }
    }

    suspend fun removeFavoriteWord(newWord: Word) {
        return withContext(Dispatchers.IO) {
            val currentSet: MutableSet<Word> = fetchFavoriteWords().toMutableSet()

            if (!currentSet.contains(newWord)) {
                throw Exception("Từ vựng không có trong danh sách yêu thích!")
            }

            currentSet.remove(newWord)
            // Cast currentSet from Set to List to save on Firebase
            getUserRef()?.child("favWords")?.setValue(currentSet.toList())?.await()
        }
    }

    suspend fun fetchFavoriteWords(): List<Word> {
        return withContext(Dispatchers.IO) {
            val list: ArrayList<Word> = ArrayList()

            val favWordsSnapshot: DataSnapshot? =
                getUserRef()?.child("favWords")?.get()?.await()

            favWordsSnapshot?.let { snapShots ->
                if (snapShots.exists()) {
                    snapShots.children.forEach { snapShot ->
                        val word = snapShot.getValue(Word::class.java)

                        word?.let { list.add(it) }
                    }
                }
            }

            list
        }
    }

    suspend fun addFavoriteGrammar(newGrammar: Grammar) {
        return withContext(Dispatchers.IO) {
            val currentSet: MutableSet<Grammar> = fetchFavoriteGrammars().toMutableSet()

            if (currentSet.contains(newGrammar)) {
                throw Exception("Ngữ pháp đã tồn tại!")
            }

            currentSet.add(newGrammar)
            getUserRef()?.child("favGrammars")?.setValue(currentSet.toList())?.await()
        }
    }

    suspend fun removeFavoriteGrammar(newGrammar: Grammar) {
        return withContext(Dispatchers.IO) {
            val currentSet: MutableSet<Grammar> = fetchFavoriteGrammars().toMutableSet()

            if (!currentSet.contains(newGrammar)) {
                throw Exception("Ngữ pháp không có trong danh sách yêu thích!")
            }

            currentSet.remove(newGrammar)
            // Cast currentSet from Set to List to save on Firebase
            getUserRef()?.child("favGrammars")?.setValue(currentSet.toList())?.await()
        }
    }

    suspend fun fetchFavoriteGrammars(): List<Grammar> {
        return withContext(Dispatchers.IO) {
            val list: ArrayList<Grammar> = ArrayList()

            val favWordsSnapshot: DataSnapshot? =
                getUserRef()?.child("favGrammars")?.get()?.await()

            favWordsSnapshot?.let { snapShots ->
                if (snapShots.exists()) {
                    snapShots.children.forEach { snapShot ->
                        val grammar = snapShot.getValue(Grammar::class.java)

                        grammar?.let { list.add(it) }
                    }
                }
            }

            list
        }
    }
}