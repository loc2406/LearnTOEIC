package com.locnguyen.toeicexercises.viewmodel.main.setting

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.model.User
import com.locnguyen.toeicexercises.repo.UserRepo
import com.locnguyen.toeicexercises.utils.Event
import kotlinx.coroutines.launch

class EditUserInfoVM : ViewModel() {
    val repo = UserRepo.getInstance()

    private val currentName: String = repo.user.value?.name ?: ""
    private val currentEmail: String = repo.user.value?.email ?: ""
    private val currentPassword: String = repo.user.value?.password ?: ""

    val newName: MutableLiveData<String> by lazy { MutableLiveData(currentName) }
    val newPassword: MutableLiveData<String> by lazy { MutableLiveData(currentPassword) }

    private val _error: MutableLiveData<String?> by lazy { MutableLiveData() }
    val error: LiveData<String?> by lazy { _error }

    private val _backClicked: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData() }
    val backClicked: LiveData<Event<Boolean>> by lazy { _backClicked }

    private val _isUpdated: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData() }
    val isUpdated: LiveData<Event<Boolean>> by lazy { _isUpdated }

    fun backClicked() {
        _backClicked.value = Event(true)
    }

    fun updateClicked() {
        val isValid = isValidNewInfo()
        if (isValid) {
            viewModelScope.launch {
                try {
                    val newInfo: MutableMap<String, String> = mutableMapOf()
                    if (newName.value != currentName) newInfo["name"] = newName.value!!.trim()
                    if (newPassword.value != currentPassword) newInfo["password"] =
                        newPassword.value!!.trim()

                    repo.updateInfo(newInfo)
                    _isUpdated.value = Event(true)
                } catch (e: Exception) {
                    Log.d("UPDATE INFO", e.message.toString())
                    _isUpdated.value = Event(false)
                }
            }
        }
    }

    private fun isValidNewInfo(): Boolean {
        return when {
            newName.value!!.isEmpty() || newPassword.value!!.isEmpty() -> {
                _error.value = "Vui lòng nhập đầy đủ các trường!"
                false
            }

            !User.isValidName(newName.value!!) -> {
                _error.value = "Tên không được chứa kí tự đặc biệt!"
                false
            }

            !User.isValidPassword(newPassword.value!!) -> {
                _error.value = "Mật khẩu phải từ 8 kí tự trở lên!"
                false
            }

            else -> {
                _error.value = null
                true
            }
        }
    }
}