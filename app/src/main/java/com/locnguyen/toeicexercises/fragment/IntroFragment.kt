package com.locnguyen.toeicexercises.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.IntroFragmentBinding

class IntroFragment: Fragment() {

    private lateinit var binding: IntroFragmentBinding
    private lateinit var mainVM: MainVM
    private lateinit var adRequest: AdRequest

    private val languages: List<String> by lazy {listOf("Tiếng Việt")}

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
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]

        adRequest = AdRequest.Builder().build()

        initViews()
        initListeners()
    }

    private fun initViews() {
        val languageAdapter = ArrayAdapter(requireContext(), R.layout.item_language, languages)
        binding.selectLanguageSpinner.adapter = languageAdapter
        binding.selectLanguageSpinner.dropDownVerticalOffset = 100

        binding.ads.loadAd(adRequest)
    }

    private fun initListeners(){
        binding.btnStartLearn.setOnClickListener{
            mainVM.startLearn.value = true
        }
    }
}