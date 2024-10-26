package com.locnguyen.toeicexercises.fragment.word

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.FavoriteWordAction
import com.locnguyen.toeicexercises.adapter.WordAdapter
import com.locnguyen.toeicexercises.databinding.ListWordFragmentBinding
import com.locnguyen.toeicexercises.databinding.WordFragmentBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.SpeakTextHelper
import com.locnguyen.toeicexercises.utils.isHasNetWork
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM

class ListWordFragment : Fragment() {

    private lateinit var binding: ListWordFragmentBinding
    private lateinit var wordAdapter: WordAdapter
    private lateinit var navController: NavController

    private val loadingDialog: Dialog by lazy { DialogHelper.getLoadingDialog(requireContext()) }
    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val wordVM: WordVM by activityViewModels<WordVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListWordFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.wordVM = wordVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

//        wordVM.fetchFavoriteWords()
        wordAdapter = WordAdapter(wordVM.words.value ?: emptyList())

        initViews()
        initListeners()
        initObserves()

        if (loadingDialog.isShowing) loadingDialog.dismiss()
    }

    private fun initViews() {
        binding.words.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = wordAdapter
        }
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handlePressedBack()
                }
            })

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onQueryTextChange(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isBlank()) {
                        wordAdapter.stopSearchAction()
                        wordVM.searchResult.value = wordVM.words.value
                    } else {
                        wordVM.handleSearchWord(it)
                    }
                }
                return true
            }
        })

        binding.icBack.setOnClickListener {
            handlePressedBack()
        }

        wordAdapter.itemClicked = { word ->
            handleItemWordClick(word)
        }

        wordAdapter.pronounceWord = { string ->
            if (requireContext().isHasNetWork()) {
                SpeakTextHelper(requireContext()).speakText(string, "en")
            } else {
                requireContext().toastMessage(R.string.Check_internet_connection)
            }
        }
    }

    private fun initObserves() {
        wordVM.favWords.observe(viewLifecycleOwner) { favoriteWords ->
            favoriteWords?.let {
                wordAdapter.favoriteWords = it
            }
        }

        wordVM.searchResult.observe(viewLifecycleOwner) { result ->
            Log.d("RESULT", result.toString())
            wordAdapter.setSearchResult(result)
        }

        wordVM.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                requireContext().toastMessage(it)
                wordVM.message.value = null
            }
        }

        wordAdapter.favoriteWordsChange.observe(viewLifecycleOwner) { change ->
            change.second?.let { word ->
                when (change.first) {
                    FavoriteWordAction.ADD -> {
                        wordVM.addFavoriteWord(word)
                        wordVM.fetchFavoriteWords()
                    }

                    FavoriteWordAction.REMOVE -> {
                        wordVM.removeFavoriteWord(word)
                        wordVM.fetchFavoriteWords()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack()
        mainVM.itemTheoryClicked.value = null
        wordVM.loadFavoriteWords.value = true
    }

    private fun handleItemWordClick(word: Word) {
        val action = ListWordFragmentDirections.actionListWordFragmentToWordFragment(word)
        navController.navigate(action)
    }
}