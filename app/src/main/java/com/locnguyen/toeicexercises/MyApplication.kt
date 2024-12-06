package com.locnguyen.toeicexercises

import android.app.Application
import com.cloudinary.android.MediaManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.locnguyen.toeicexercises.database.TheoryDb
import com.locnguyen.toeicexercises.utils.CloudinaryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication: Application() {

    private lateinit var db: TheoryDb

    override fun onCreate() {
        super.onCreate()

        instance = this
        FirebaseApp.initializeApp(this)
        db = TheoryDb(applicationContext)
        MobileAds.initialize(this@MyApplication)
        MediaManager.init(this, CloudinaryManager.getConfig())
    }

    fun getDbInstance() = db

    companion object{
        lateinit var instance: MyApplication
    }
}