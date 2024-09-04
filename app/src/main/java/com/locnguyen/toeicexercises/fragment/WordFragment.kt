package com.locnguyen.toeicexercises.fragment

import android.Manifest
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.toeicexercises.MainActivity
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.WordAdapter
import com.locnguyen.toeicexercises.databinding.WordFragmentBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.WordDB
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM

class WordFragment: Fragment() {

    private lateinit var binding: WordFragmentBinding
    private lateinit var wordAdapter: WordAdapter
    private lateinit var mainVM: MainVM
    private lateinit var wordVM: WordVM
    private lateinit var navController: NavController

    private val loadingDialog: Dialog by lazy { DialogHelper(requireContext()).getLoadingDialog() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WordFragmentBinding.inflate(inflater, container, false)
        loadingDialog.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val words: List<Word> = WordDB(requireContext()).getListWord(1)
        wordAdapter = WordAdapter(words) { word ->
            handleItemWordClick(word)
        }
        loadingDialog.dismiss()

        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        wordVM = ViewModelProvider(this)[WordVM::class.java]
        navController = Navigation.findNavController(view)

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        binding.title.text = requireContext().getString(R.string.Word)
        binding.words.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = wordAdapter
        }
    }

    private fun initListeners(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                handlePressedBack()
            }
        })

        binding.icBack.setOnClickListener {
            handlePressedBack()
        }
    }

    private  fun initObserves(){
    }

    private fun handlePressedBack(){
        navController.popBackStack()
        mainVM.itemTheoryClicked.value = null
    }

    private fun handleItemWordClick(word: Word){
        DialogHelper(requireContext()).getWordMeanDialog(word).show()
    }
}