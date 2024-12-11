package com.locnguyen.toeicexercises.repo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepo {
    companion object {
        @Volatile
        private var instance: UserRepo? = null
        private var isInitialized = false

        fun init() {
            if (!isInitialized){
                instance = UserRepo().also { instance = it }
                isInitialized = true
            }
        }

        fun getInstance(): UserRepo {
            return instance ?: synchronized(this) {
                init()
                instance!!
            }
        }
    }

    private val db: FirebaseDatabase by lazy { Firebase.database }
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val usersDbRef: DatabaseReference by lazy { db.getReference("users") }
    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    suspend fun refreshUser() {
        return withContext(Dispatchers.IO) {
            try {
                _user.postValue(getUserRef()?.get()?.await()?.getValue(User::class.java))
            } catch (e: Exception) {
                Log.d("REFRESH_USER", e.message.toString())
            }
        }
    }

    suspend fun login(
        email: String,
        password: String,
    ): User? {
        return withContext(Dispatchers.IO) {
            var userFound: User? = null
            var isEmailFound: Boolean = false
            val usersSnapshot: DataSnapshot? = try {
                usersDbRef.get().await()
            } catch (e: Exception) {
                throw Exception("Có lỗi xảy ra! Vui lòng thử lại!")
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
                                        throw Exception("Đăng nhập vào Firebase thất bại!")
                                    }
                                } else {
                                    throw Exception("Sai mật khẩu!")
                                }
                            }
                        }
                    }

                    if (!isEmailFound) {
                        throw Exception("Không tìm thấy tài khoản!")
                    }
                } else {
                    throw Exception("Không tìm thấy tài khoản!")
                }
            }
            userFound
        }
    }

    fun getUserRef(): DatabaseReference? {
        val userRef: String? = auth.currentUser?.email?.substringBefore('@')
        return if (userRef != null) {
            usersDbRef.child(userRef)
        } else {
            null
        }
    }

    suspend fun createNewUser(
        email: String,
        name: String,
        password: String
    ): User {
        return withContext(Dispatchers.IO) {
            val isCreated = checkIfUserExist(email)

            if (isCreated) {
                throw Exception("Tài khoản đã tồn tại!")
            }

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            if (authResult.user == null) {
                auth.currentUser?.delete()
                throw Exception("Tạo tài khoản mới thất bại!")
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
            usersDbRef.get().await()
        } catch (e: Exception) {
            throw Exception("Có lỗi xảy ra! Vui lòng thử lại!")
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

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            auth.signOut()
        }
    }

    suspend fun updateNewImg(newImg: Uri): String =
        suspendCancellableCoroutine { continuation -> //chuyển callback thành coroutine
            try {
                MediaManager.get().upload(newImg)
                    .options(
                        mapOf(
                            Pair("public_id", "user_img")
                        )
                    )
                    .callback(object : UploadCallback {
                        override fun onStart(requestId: String?) {
                            Log.d("updateNewImg", "onStart")
                        }

                        override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                            Log.d("updateNewImg", "onProgress: ${bytes * 100 / totalBytes}%")
                        }

                        override fun onSuccess(
                            requestId: String?,
                            resultData: MutableMap<Any?, Any?>?
                        ) {
                            Log.d("updateNewImg", "onSuccess")
                            val imageUrl = resultData?.get("secure_url")?.toString()
                            if (imageUrl != null) {
                                getUserRef()?.child("avatar")?.setValue(imageUrl)
                                continuation.resume(imageUrl)
                            } else {
                                continuation.resumeWithException(Exception("Upload failed: URL is null"))
                            }
                        }

                        override fun onError(requestId: String?, error: ErrorInfo?) {
                            Log.e("updateNewImg", "onError: ${error?.description}")
                            continuation.resumeWithException(
                                Exception(
                                    error?.description ?: "Upload failed"
                                )
                            )
                        }

                        override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                            Log.d("updateNewImg", "onReschedule")
                            continuation.resumeWithException(Exception("Upload rescheduled"))
                        }
                    }).dispatch()

                // Nếu cancel thì hủy upload
//            continuation.invokeOnCancellation {
//                // Có thể thêm logic hủy upload ở đây nếu cần
//            }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }

    suspend fun isExistedEmail(email: String): Boolean{
        return withContext(Dispatchers.IO){
            try{
                val signInMethods = auth.fetchSignInMethodsForEmail(email).await().signInMethods
                signInMethods?.isNotEmpty() ?: false
            }catch (e: Exception){
                throw e
            }
        }
    }

    suspend fun updateInfo(newInfo: Map<String, String>) {
        return withContext(Dispatchers.IO) {
            try {
                getUserRef()?.updateChildren(newInfo)?.await() ?: {
                    throw Exception("Không tìm thấy user!")
                }
                refreshUser()
            } catch (e: Exception) {
                throw Exception("Cập nhật thông tin thất bại: ${e.message}")
            }
        }
    }
}