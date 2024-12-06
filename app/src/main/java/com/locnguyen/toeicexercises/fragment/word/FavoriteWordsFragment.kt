package com.locnguyen.toeicexercises.fragment.word

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.WordNoteAdapter
import com.locnguyen.toeicexercises.databinding.FavoriteWordsFragmentBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.SpeakTextHelper
import com.locnguyen.toeicexercises.utils.isHasNetWork
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.main.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM

class FavoriteWordsFragment : Fragment() {
    private lateinit var binding: FavoriteWordsFragmentBinding
    private lateinit var wordNoteAdapter: WordNoteAdapter
    private lateinit var navController: NavController

    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val wordVM: WordVM by activityViewModels<WordVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoriteWordsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        wordNoteAdapter = WordNoteAdapter()
        loadFavoriteWords()

        initViews()
        initListeners()
        initObserves()
    }

    private fun loadFavoriteWords() {
        wordVM.fetchFavoriteWords()
    }

    private fun initViews() {
        binding.favoriteWords.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = wordNoteAdapter
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

        binding.icBack.setOnClickListener {
            handlePressedBack()
        }

        if (::wordNoteAdapter.isInitialized) {
            wordNoteAdapter.pronounceClicked = { pronounce ->
                if (requireContext().isHasNetWork()) {
                    SpeakTextHelper(requireContext()).speakText(pronounce, "en")
                } else {
                    requireContext().toastMessage(R.string.Check_internet_connection)
                }
            }

            wordNoteAdapter.itemClicked = { word ->
                handleItemWordClicked(word)
            }
        }
    }

    private fun initObserves() {
        wordVM.loadFavoriteWords.observe(viewLifecycleOwner){needLoad ->
            needLoad.takeIf { it != null }?.let { wordVM.fetchFavoriteWords() }
        }

        wordVM.favWords.observe(viewLifecycleOwner) { favoriteWords ->
            favoriteWords?.let {
                if (it.isEmpty()) {
                    binding.dontHaveDataMessage.visibility = VISIBLE
                    binding.favoriteWords.visibility = INVISIBLE
                } else {
                    wordNoteAdapter.updateFavoriteList(it)
                }
            }
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack()
        mainVM.wordsNoteClicked.value = false
        wordVM.loadFavoriteWords.value = true
    }

    private fun handleItemWordClicked(word: Word) {
        val direction =
            FavoriteWordsFragmentDirections.actionFavoriteWordsFragmentToWordFragment(word)
        navController.navigate(direction)
    }
}