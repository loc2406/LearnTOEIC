package com.locnguyen.toeicexercises.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdRequest
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.IntroFragmentBinding

class IntroFragment: Fragment() {

    private lateinit var binding: IntroFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = IntroFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initListeners()
    }

    private fun initListeners(){
        binding.btnStartLearn.setOnClickListener{
            navController.navigate(R.id.action_introFragment_to_mainFragment)
        }
    }
}