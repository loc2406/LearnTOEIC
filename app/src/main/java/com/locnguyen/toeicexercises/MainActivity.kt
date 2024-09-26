package com.locnguyen.toeicexercises

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.locnguyen.toeicexercises.databinding.MainActivityBinding
import com.locnguyen.toeicexercises.database.TheoryDB
import com.locnguyen.toeicexercises.viewmodel.GrammarVM
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var mainVM: MainVM
    private lateinit var wordVM: WordVM
    private lateinit var grammarVM: GrammarVM
    private lateinit var theoryDb: TheoryDB
//    private var adsInterval: AdsInterval? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }

        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.primary);
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white);

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController

        mainVM = ViewModelProvider(this)[MainVM::class.java]
        wordVM = ViewModelProvider(this)[WordVM::class.java]
        grammarVM = ViewModelProvider(this)[GrammarVM::class.java]

        if (navController.currentDestination?.id == R.id.introFragment) {
            splashScreen.setKeepOnScreenCondition { false }
        }

        theoryDb = TheoryDB(this)
        wordVM.words.value = theoryDb.getListWord()
        wordVM.examples.value = theoryDb.getListExamples()
        grammarVM.grammars.value = theoryDb.getListGrammars()


//        adsInterval = AdsInterval(this)
    }

//    private fun onShowAdsInterval(){
//        try{
//            adsInterval?.showIntervalAds()
//        }catch (e: OutOfMemoryError){
//            e.printStackTrace()
//        }
//    }
}