package com.locnguyen.toeicexercises.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.locnguyen.toeicexercises.Api
import com.locnguyen.toeicexercises.adapter.ExerciseAdapter
import com.locnguyen.toeicexercises.adapter.TheoryAdapter
import com.locnguyen.toeicexercises.databinding.PracticeFragmentBinding
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.viewmodel.GrammarVM
import com.locnguyen.toeicexercises.viewmodel.MainVM
import com.locnguyen.toeicexercises.viewmodel.WordVM
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PracticeFragment: Fragment() {

    private lateinit var binding: PracticeFragmentBinding
    private lateinit var theoryAdapter: TheoryAdapter
    private lateinit var exerciseAdapter: ExerciseAdapter

    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val wordVM: WordVM by activityViewModels<WordVM>()
    private val grammarVM: GrammarVM by activityViewModels<GrammarVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PracticeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theoryAdapter = TheoryAdapter(listOf("Từ vựng", "Ngữ pháp"))
        exerciseAdapter = ExerciseAdapter(listOf("Chọn câu đúng", "Nối câu"))

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        binding.ads.loadAd(AdRequest.Builder().build())

        binding.theoryContent.apply {
            adapter = theoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.exerciseContent.apply {
            adapter = exerciseAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun loadFavoriteWords(){
        binding.wordQuantity.text = (wordVM.favWords.value?.size ?: 0).toString()
    }

    private fun loadFavoriteGrammars() {
        binding.grammarQuantity.text = grammarVM.getFavoriteGrammars().size.toString()
    }

    private fun initListeners(){
        theoryAdapter.itemClicked = { typeName ->
            DialogHelper.getLoadingDialog(requireActivity()).show()
            mainVM.itemTheoryClicked.value = typeName
        }

        exerciseAdapter.itemClicked = { exerciseName ->
            mainVM.itemExerciseClicked.value = exerciseName
        }

        binding.noteWordSpace.setOnClickListener {
            mainVM.wordsNoteClicked.value = true
        }

        binding.noteGrammarSpace.setOnClickListener {
            mainVM.grammarsNoteClicked.value = true
        }
    }

    private fun initObserves(){
        wordVM.loadFavoriteWords.observe(viewLifecycleOwner){ needLoad ->
            needLoad.takeIf { it == true }?.let{
                wordVM.fetchFavoriteWords()
                loadFavoriteWords()
            }
        }

        grammarVM.loadFavoriteGrammars.observe(viewLifecycleOwner){ needLoad ->
            needLoad.takeIf { it == true }?.let{ loadFavoriteGrammars() }
        }
    }
}