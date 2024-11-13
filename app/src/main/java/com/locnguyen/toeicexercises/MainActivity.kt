package com.locnguyen.toeicexercises

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.locnguyen.toeicexercises.databinding.MainActivityBinding
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.GrammarVM
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var splashScreen: SplashScreen

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen().apply {
            setKeepOnScreenCondition{true}
        }
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.primary);
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white);

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController

        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handlePressedBack()
            }
        })

        initData()
    }

    private fun initData() {
        lifecycleScope.launch {
            DataManager.initData(this@MainActivity)

            DataManager.isDataLoaded.collect{ isLoaded ->
                if (isLoaded){
                    splashScreen.setKeepOnScreenCondition{false}
                }
                else{
                    DataManager.err.value?.let { this@MainActivity.toastMessage(it) }
                    finish()
                }
            }
        }
    }

    private fun handlePressedBack(){
        if (backPressedTime + 5000 > System.currentTimeMillis()) {
            onBackPressedDispatcher.onBackPressed()
            return
        } else {
            toastMessage(R.string.Exit_app_message)
        }
        backPressedTime = System.currentTimeMillis()
    }
}