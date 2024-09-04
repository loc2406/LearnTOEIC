package com.locnguyen.toeicexercises.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.locnguyen.toeicexercises.adapter.AboutAppAdapter
import com.locnguyen.toeicexercises.databinding.AboutAppFragmentBinding
import com.locnguyen.toeicexercises.databinding.MainFragmentBinding
import com.locnguyen.toeicexercises.databinding.SettingFragmentBinding

class SettingFragment: Fragment() {

    private lateinit var binding: SettingFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
    }
}