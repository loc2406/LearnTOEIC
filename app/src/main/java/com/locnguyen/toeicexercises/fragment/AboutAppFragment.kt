package com.locnguyen.toeicexercises.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.locnguyen.toeicexercises.adapter.AboutAppAdapter
import com.locnguyen.toeicexercises.databinding.AboutAppFragmentBinding
import com.locnguyen.toeicexercises.databinding.MainFragmentBinding

class AboutAppFragment: Fragment() {

    private lateinit var binding: AboutAppFragmentBinding
    private lateinit var aboutAppAdapter: AboutAppAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AboutAppFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        aboutAppAdapter = AboutAppAdapter(requireActivity())
        binding.aboutAppViewPager.adapter = aboutAppAdapter
    }
}