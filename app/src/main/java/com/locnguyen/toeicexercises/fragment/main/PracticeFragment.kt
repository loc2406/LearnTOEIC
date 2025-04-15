package com.locnguyen.toeicexercises.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.PracticeFragmentBinding
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.Event
import com.locnguyen.toeicexercises.viewmodel.GrammarVM
import com.locnguyen.toeicexercises.viewmodel.main.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM
import com.locnguyen.toeicexercises.viewmodel.main.practice.PracticeVM

class PracticeFragment: Fragment() {

    private lateinit var binding: PracticeFragmentBinding

    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val practiceVM: PracticeVM by viewModels<PracticeVM>()
    private val wordVM: WordVM by activityViewModels<WordVM>()
    private val grammarVM: GrammarVM by activityViewModels<GrammarVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PracticeFragmentBinding.inflate(inflater, container, false)
        binding.practiceVM = practiceVM
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        binding.ads.loadAd(AdRequest.Builder().build())
        loadFavoriteWords()
        loadFavoriteGrammars()
    }

    private fun loadFavoriteWords(){
        wordVM.fetchFavoriteWords()
        displayFavWords()
    }

    private fun displayFavWords(){
        binding.wordQuantity.text = (wordVM.favWords.value?.size ?: 0).toString()
    }

    private fun loadFavoriteGrammars() {
        grammarVM.fetchFavoriteGrammars()
        displayFavGrammars()
    }

    private fun displayFavGrammars(){
        binding.grammarQuantity.text = (grammarVM.favGrammars.value?.size ?: 0).toString()
    }

    private fun initListeners(){
        binding.noteWordSpace.setOnClickListener {
            mainVM.wordsNoteClicked.value = true
        }

        binding.noteGrammarSpace.setOnClickListener {
            mainVM.grammarsNoteClicked.value = true
        }
    }

    private fun initObserves(){
        practiceVM.wordClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                DialogHelper.getLoadingDialog(requireActivity()).show()
                mainVM.itemTheoryClicked.value = Event(requireContext().getString(R.string.Word))
            }
        }

        practiceVM.grammarClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                DialogHelper.getLoadingDialog(requireActivity()).show()
                mainVM.itemTheoryClicked.value = Event(requireContext().getString(R.string.Grammar))
            }
        }

        practiceVM.selectAnswerClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                DialogHelper.getLoadingDialog(requireActivity()).show()
                mainVM.itemExerciseClicked.value = Event(requireContext().getString(R.string.Select_answer))
            }
        }

        practiceVM.readParagraphClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                DialogHelper.getLoadingDialog(requireActivity()).show()
                mainVM.itemExerciseClicked.value = Event(requireContext().getString(R.string.Read_paragraph))
            }
        }

        wordVM.isNeedLoaded.observe(viewLifecycleOwner){ eventLoaded ->
            eventLoaded?.getContentIfNotHandled().let{
                loadFavoriteWords()
            }
        }

        wordVM.favWords.observe(viewLifecycleOwner){ list ->
            list?.let{displayFavWords()}
        }

        grammarVM.isNeedLoaded.observe(viewLifecycleOwner){ eventLoaded ->
            eventLoaded?.getContentIfNotHandled().let{
                loadFavoriteGrammars()
            }
        }

        grammarVM.favGrammars.observe(viewLifecycleOwner){ list ->
            list?.let{displayFavGrammars()}
        }
    }
}