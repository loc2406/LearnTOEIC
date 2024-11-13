package com.locnguyen.toeicexercises.repo

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DataRepo(private val context: Context) {

    private val examsDbRef: DatabaseReference by lazy { fbDbInstance.getReference("exams") }
    private val fbDbInstance: FirebaseDatabase by lazy { Firebase.database }
    private val usersDbRef: DatabaseReference by lazy { fbDbInstance.getReference("users") }

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

    suspend fun addFavoriteWord(newWord: Word): Boolean {
        return withContext(Dispatchers.IO) {
            val currentSet: MutableSet<Word> = fetchFavoriteWords().toMutableSet()
            val userEmail: String = Firebase.auth.currentUser?.email
                ?: throw Exception(context.getString(R.string.Unexpected_error_occurred))
            val userRef = usersDbRef.child(userEmail.substringBefore('@'))

            if (currentSet.contains(newWord)) {
                throw Exception(context.getString(R.string.Word_already_exists))
            }

            try {
                currentSet.add(newWord)
                // Cast currentSet from Set to List to save on Firebase
                userRef.child("favWords").setValue(currentSet.toList()).await()
                true
            } catch (e: Exception) {
                throw Exception(context.getString(R.string.Added_favorite_word_failed))
            }
        }
    }

    suspend fun removeFavoriteWord(newWord: Word): Boolean {
        return withContext(Dispatchers.IO) {
            val currentSet: MutableSet<Word> = fetchFavoriteWords().toMutableSet()
            val userEmail: String = Firebase.auth.currentUser?.email
                ?: throw Exception(context.getString(R.string.Unexpected_error_occurred))
            val userRef = usersDbRef.child(userEmail.substringBefore('@'))

            if (!currentSet.contains(newWord)) {
                throw Exception(context.getString(R.string.Word_not_exists))
            }

            try {
                currentSet.remove(newWord)
                // Cast currentSet from Set to List to save on Firebase
                userRef.child("favWords").setValue(currentSet.toList()).await()
                true
            } catch (e: Exception) {
                throw Exception(context.getString(R.string.Removed_favorite_word_failed))
            }
        }
    }

    suspend fun fetchFavoriteWords(): List<Word> {
        return withContext(Dispatchers.IO) {
            val list: ArrayList<Word> = ArrayList()
            val userEmail: String = Firebase.auth.currentUser?.email
                ?: throw Exception(context.getString(R.string.Unexpected_error_occurred))
            val userRef = usersDbRef.child(userEmail.substringBefore('@'))

            val favWordsSnapshot: DataSnapshot? =
                userRef.child("favWords").get().await()

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
}