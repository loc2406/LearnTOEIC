package com.locnguyen.toeicexercises.fragment.grammar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.GrammarAdapter
import com.locnguyen.toeicexercises.adapter.GrammarLessonAdapter
import com.locnguyen.toeicexercises.databinding.ListGrammarFragmentBinding
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.viewmodel.GrammarVM
import com.locnguyen.toeicexercises.viewmodel.MainVM

class ListGrammarFragment: Fragment() {
    private lateinit var binding: ListGrammarFragmentBinding
    private lateinit var navController: NavController
    private lateinit var grammarLessonAdapter: GrammarLessonAdapter
    private lateinit var grammarAdapter: GrammarAdapter
    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val grammarVM: GrammarVM by activityViewModels<GrammarVM>()

    private val loadingDialog: Dialog by lazy { DialogHelper.getLoadingDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListGrammarFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val grammarLessons = grammarVM.grammars.value?.map{ grammar -> grammar.level}?.toSet()?.toList() ?: emptyList()
        grammarLessonAdapter = GrammarLessonAdapter(grammarLessons){ lesson ->
            grammarVM.currentLesson.value = lesson
        }
        grammarAdapter = GrammarAdapter(grammarVM.grammars.value ?: emptyList())

        initViews()
        initListeners()
        initObserves()

        if (loadingDialog.isShowing) loadingDialog.dismiss()
    }

    private fun initViews() {
        binding.title.text = requireContext().getString(R.string.Grammar)
        binding.lessons.apply {
           layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = grammarLessonAdapter
        }

        binding.grammars.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = grammarAdapter
        }
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                handlePressedBack()
            }
        })

        binding.icBack.setOnClickListener {
            handlePressedBack()
        }

        grammarAdapter.itemClicked = { grammar ->
            loadingDialog.show()
            handleGrammarClicked(grammar)
        }
    }

    private fun initObserves(){
        grammarVM.currentLesson.observe(viewLifecycleOwner){ lesson ->
            lesson?.let{
                grammarLessonAdapter.setCurrentLesson(it)
                grammarAdapter.setData(it)
            }
        }
    }

    private fun handlePressedBack(){
        navController.popBackStack()
        mainVM.itemTheoryClicked.value = null
        grammarVM.loadFavoriteGrammars.value = true
    }

    private fun handleGrammarClicked(grammar: Grammar) {
        val action = ListGrammarFragmentDirections.actionListGrammarFragmentToGrammarFragment(grammar)
        navController.navigate(action)
    }
}