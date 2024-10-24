package com.locnguyen.toeicexercises.repo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.User
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.utils.toastMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepo(private val context: Context) {

    private val fbDbInstance: FirebaseDatabase by lazy { Firebase.database }
    private val usersDbRef: DatabaseReference by lazy { fbDbInstance.getReference("users") }
    private var user: User? = null

    suspend fun findUserInFb(
        email: String,
        password: String,
    ): User? {
        return withContext(Dispatchers.IO) {
            var userFound: User? = null
            var isEmailFound: Boolean = false
            val usersSnapshot: DataSnapshot? = try {
                getAllUserInFb()
            } catch (e: Exception) {
                throw Exception(context.getString(R.string.Something_went_wrong_please_try_again))
            }

            usersSnapshot?.let {
                if (it.exists()) {
                    it.children.forEach { userSnapshot ->
                        val user: User? = userSnapshot.getValue(User::class.java)

                        user?.let { u ->
                            if (u.email == email) {
                                isEmailFound = true
                                if (u.password == password) {
                                    userFound = u
                                    this@UserRepo.user = u
                                } else {
                                    throw Exception(context.getString(R.string.Wrong_password))
                                }
                            }
                        }
                    }

                    if (!isEmailFound) {
                        throw Exception(context.getString(R.string.Account_not_found))
                    }
                } else {
                    throw Exception(context.getString(R.string.Account_not_found))
                }
            }
            userFound
        }
    }

    private suspend fun getAllUserInFb(): DataSnapshot? = usersDbRef.get().await()

    suspend fun createNewUser(
        email: String,
        name: String,
        password: String
    ): User {
        return withContext(Dispatchers.IO) {
            val user: User
            val isCreated = checkIfUserExist(email)

            if (!isCreated) {
                user = User(email, name, password)
                this@UserRepo.user = user
                usersDbRef.child(email.substringBefore('@')).setValue(user)
            } else {
                throw Exception(context.getString(R.string.Acount_is_existed))
            }

            user
        }
    }

    private suspend fun checkIfUserExist(
        email: String
    ): Boolean {
        val usersSnapshot: DataSnapshot? = try {
            getAllUserInFb()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.Something_went_wrong_please_try_again))
        }

        usersSnapshot?.let {
            if (it.exists()) {
                for (userSnapshot in it.children) {
                    val user: User? = userSnapshot.getValue(User::class.java)
                    if (user?.email == email) {
                        return true
                    }
                }
            }
        }

        return false
    }

    suspend fun addFavoriteWord(newWord: Word): Boolean {
        return withContext(Dispatchers.IO) {
            var isAdded: Boolean = false
            val list: ArrayList<Word> = ArrayList()
            val userEmail = this@UserRepo.user?.email

            try {
                userEmail?.let { email ->
                    val userRef = usersDbRef.child(email.substringBefore('@'))
                    val favWordsSnapshot: DataSnapshot? =
                        userRef.child("favWords").get().await()

                    favWordsSnapshot?.let { snapShots ->
                        if (snapShots.exists()) {
                            snapShots.children.forEach { snapShot ->
                                val word = snapShot.getValue(Word::class.java)

                                word?.let { list.add(it) }
                            }
                        }

                        list.takeIf { !it.contains(newWord) }?.add(newWord)
                    }

                    Tasks.await(userRef.child("favWords").setValue(list))
                    isAdded = true
                }
            } catch (e: Exception) {
                throw Exception(context.getString(R.string.Added_favorite_word_failed))
            }

            isAdded
        }
    }

    suspend fun removeFavoriteWord(newWord: Word): Boolean {
        return withContext(Dispatchers.IO) {
            var isRemoved: Boolean = false
            val list: ArrayList<Word> = ArrayList()
            val userEmail = this@UserRepo.user?.email

            try {
                userEmail?.let { email ->
                    val userRef = usersDbRef.child(email.substringBefore('@'))
                    val favWordsSnapshot: DataSnapshot? =
                        userRef.child("favWords").get().await()

                    favWordsSnapshot?.let { snapShots ->
                        if (snapShots.exists()) {
                            snapShots.children.forEach { snapShot ->
                                val word = snapShot.getValue(Word::class.java)

                                word?.let { list.add(it) }
                            }
                        }

                        list.takeIf { !it.contains(newWord) }?.add(newWord)
                    }

                    Tasks.await(userRef.child("favWords").setValue(list))
                    isRemoved = true
                }
            } catch (e: Exception) {
                throw Exception(context.getString(R.string.Removed_favorite_word_failed))
            }

            isRemoved
        }
    }

    suspend fun fetchFavoriteWords(): List<Word> {
        return withContext(Dispatchers.IO){
            val list: ArrayList<Word> = ArrayList()
            val userEmail = this@UserRepo.user?.email

            try {
                userEmail?.let { email ->
                    val userRef = usersDbRef.child(email.substringBefore('@'))
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
                }
            } catch (e: Exception) {
                throw Exception(context.getString(R.string.Get_favorite_word_failed))
            }

            list
        }
    }
}