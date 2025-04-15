package com.locnguyen.toeicexercises.viewmodel.main.setting

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.locnguyen.toeicexercises.model.User
import com.locnguyen.toeicexercises.repo.UserRepo
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.utils.Event
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.launch

class SettingVM(val app: Application) : AndroidViewModel(app) {

    val repo: UserRepo by lazy { UserRepo.getInstance() }
    val user: LiveData<User> by lazy { repo.user }

    private val _changeImgClicked: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData() }
    val changeImgClicked: LiveData<Event<Boolean>> by lazy { _changeImgClicked }

    private val _logoutClicked: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData() }
    val logoutClicked: LiveData<Event<Boolean>> by lazy { _logoutClicked }

    private val _editInfoClicked: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData() }
    val editInfoClicked: LiveData<Event<Boolean>> by lazy { _editInfoClicked }

    private val _imgUrl: MutableLiveData<String?> by lazy { MutableLiveData() }
    val imgUrl: LiveData<String?> by lazy { _imgUrl }

    fun logoutClicked() {
        _logoutClicked.value = Event(true)
    }

    fun changeImgClicked() {
        _changeImgClicked.value = Event(true)
    }

    fun editInfoClicked() {
        _editInfoClicked.value = Event(true)
    }

    fun updateNewImg(newImg: Uri) {
        viewModelScope.launch {
            try {
                val resultUrl = repo.updateNewImg(newImg)
                _imgUrl.value = resultUrl
            } catch (e: Exception) {
                _imgUrl.value = null
            }
        }
    }
}