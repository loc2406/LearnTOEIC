package com.locnguyen.toeicexercises.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.ktx.Firebase
import com.locnguyen.toeicexercises.databinding.MainFragmentBinding
import com.locnguyen.toeicexercises.databinding.SettingFragmentBinding
import com.locnguyen.toeicexercises.model.User
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.viewmodel.MainVM

class SettingFragment: Fragment() {

    private lateinit var binding: SettingFragmentBinding
    private val mainVM: MainVM by activityViewModels<MainVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        binding.mainVM = mainVM
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserves()
    }

    private fun initObserves() {
    }
}