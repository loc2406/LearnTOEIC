package com.locnguyen.toeicexercises.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.AboutAppFragment1Binding

class AboutAppFragment1: Fragment() {

    private lateinit var binding: AboutAppFragment1Binding
    private lateinit var mainVM: MainVM

    private val languages: List<String> by lazy {listOf("Tiếng Việt")}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AboutAppFragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]

        initViews()
        initListeners()
    }

    private fun initViews() {
        val languageAdapter = ArrayAdapter(requireContext(), R.layout.item_language, languages)
        binding.selectLanguageSpinner.adapter = languageAdapter
        binding.selectLanguageSpinner.dropDownVerticalOffset = 100
    }

    private fun initListeners(){
        binding.btnStartLearn.setOnClickListener{
            mainVM.startLearn.value = true
        }
    }
}