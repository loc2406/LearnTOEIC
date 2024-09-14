package com.locnguyen.toeicexercises

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.locnguyen.toeicexercises.databinding.MainActivityBinding
import com.locnguyen.toeicexercises.ui.theme.TOEICExercisesTheme
import com.locnguyen.toeicexercises.utils.WordDB
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var mainVM: MainVM
    private lateinit var wordVM: WordVM

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{true}

        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.primary);
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary);

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController
        mainVM = ViewModelProvider(this)[MainVM::class.java]
        wordVM = ViewModelProvider(this)[WordVM::class.java]

        if(navController.currentDestination?.id == R.id.aboutAppFragment){
            splashScreen.setKeepOnScreenCondition{false}
        }

        wordVM.words.value = WordDB(this).getListWord()
        wordVM.examples.value = WordDB(this).getListExamples()

        initObserves()
    }

    private fun initObserves() {
        mainVM.startLearn.observe(this){ isClicked ->
            if (isClicked){
                navController.navigate(R.id.action_aboutAppFragment_to_mainFragment)
            }
        }
    }
}