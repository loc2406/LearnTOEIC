package com.locnguyen.toeicexercises.repo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
    private val fbAuthInstance: FirebaseAuth by lazy { Firebase.auth }
    private val usersDbRef: DatabaseReference by lazy { fbDbInstance.getReference("users") }

    suspend fun login(
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
                                    val loginTask =
                                        Firebase.auth.signInWithEmailAndPassword(email, password)
                                            .await()

                                    if (loginTask.user != null) {
                                        userFound = u
                                    } else {
                                        throw Exception(context.getString(R.string.Login_firebase_failed))
                                    }
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
            val isCreated = checkIfUserExist(email)

            if (isCreated) {
                throw Exception(context.getString(R.string.Account_is_existed))
            }

            val authResult = fbAuthInstance.createUserWithEmailAndPassword(email, password).await()

            if (authResult.user == null) {
                fbAuthInstance.currentUser?.delete()
                throw Exception(context.getString(R.string.Create_new_account_failed))
            }

            val user: User = User(email, name, password)
            usersDbRef.child(email.substringBefore('@')).setValue(user).await()

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
}