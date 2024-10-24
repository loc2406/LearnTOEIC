package com.locnguyen.toeicexercises

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.locnguyen.toeicexercises.viewmodel.WordVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val backgroundScope = CoroutineScope(Dispatchers.IO)

        backgroundScope.launch {
            MobileAds.initialize(this@MyApplication)
        }
    }
}