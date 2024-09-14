package com.locnguyen.toeicexercises.fragment.word

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.WordAdapter
import com.locnguyen.toeicexercises.databinding.WordFragmentBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM

class WordFragment: Fragment() {

    private lateinit var binding: WordFragmentBinding
    private lateinit var wordAdapter: WordAdapter
    private lateinit var mainVM: MainVM
    private lateinit var wordVM: WordVM
    private lateinit var navController: NavController

    private val loadingDialog: Dialog by lazy { DialogHelper.getLoadingDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        wordVM = ViewModelProvider(requireActivity())[WordVM::class.java]
        navController = Navigation.findNavController(view)

        binding.lifecycleOwner = this
        binding.wordVM = wordVM

        if (loadingDialog.isShowing) loadingDialog.dismiss()

        val allWords = wordVM.words.value ?: emptyList()

        wordAdapter = WordAdapter(allWords) { word ->
            handleItemWordClick(word)
        }

        wordVM.searchResult.value = allWords

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

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               onQueryTextChange(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    if (it.isBlank()){
                        wordAdapter.stopSearchAction()
                        wordVM.searchResult.value = wordVM.words.value
                    }
                    else{
                        wordVM.handleSearchWord(it)
                    }
                }
                return true
            }
        })

        binding.icBack.setOnClickListener {
            handlePressedBack()
        }
    }

    private  fun initObserves(){
        wordVM.searchResult.observe(viewLifecycleOwner){ result ->
            Log.d("ABCXYZ", result.size.toString() + "----" + wordAdapter.defaultList.size.toString())
            wordAdapter.setSearchResult(result)
        }
    }

    private fun handlePressedBack(){
        navController.popBackStack()
        mainVM.itemTheoryClicked.value = null
    }

    private fun handleItemWordClick(word: Word){
        val action = WordFragmentDirections.actionWordFragmentToListMeansFragment(word)
        navController.navigate(action)
    }
}