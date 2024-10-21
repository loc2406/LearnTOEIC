package com.locnguyen.toeicexercises.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.User
import com.locnguyen.toeicexercises.repo.UserRepo
import kotlinx.coroutines.launch

class RegisterVM(val app: Application) : AndroidViewModel(app) {
    private val userRepo: UserRepo by lazy { UserRepo(app) }
    val email: MutableLiveData<String> by lazy { MutableLiveData("") }
    val name: MutableLiveData<String> by lazy { MutableLiveData("") }
    val password: MutableLiveData<String> by lazy { MutableLiveData("") }
    val confirmPassword: MutableLiveData<String> by lazy { MutableLiveData("") }
    val errMessage: MutableLiveData<String?> by lazy { MutableLiveData() }

    val isValidInfo: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val user: MutableLiveData<User> by lazy { MutableLiveData() }

    fun handleBtnRegisterClicked() {
        isValidInfo.value = isValidInfo()
    }

    private fun isValidInfo(): Boolean {
        return when {
            email.value!!.isBlank() -> {
                errMessage.value = app.getString(R.string.Email_can_not_empty)
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches() -> {
                errMessage.value = app.getString(R.string.Invalid_email)
                false
            }

            name.value!!.isBlank() -> {
                errMessage.value = app.getString(R.string.Username_can_not_empty)
                false
            }

            password.value!!.isBlank() -> {
                errMessage.value = app.getString(R.string.Password_can_not_empty)
                false
            }

            password.value!!.length < 8 -> {
                errMessage.value = app.getString(R.string.Password_must_more_than_8_chars)
                false
            }

            confirmPassword.value != password.value -> {
                errMessage.value = app.getString(R.string.Confirm_password_not_correct)
                false
            }

            else -> {
                errMessage.value = null
                true
            }
        }
    }

    fun createNewUser() {
        viewModelScope.launch {
            val result: User? = try{
                userRepo.createNewUser(email.value!!, name.value!!, password.value!!)
            }catch (e: Exception){
                errMessage.value = e.message
                null
            }
            user.postValue(result)
        }
    }
}