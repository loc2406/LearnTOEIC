package com.locnguyen.toeicexercises.fragment.grammar

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.toeicexercises.adapter.GrammarNoteAdapter
import com.locnguyen.toeicexercises.databinding.FavoriteGrammarsFragmentBinding
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.viewmodel.GrammarVM
import com.locnguyen.toeicexercises.viewmodel.main.MainVM

class FavoriteGrammarsFragment: Fragment() {
    private lateinit var binding: FavoriteGrammarsFragmentBinding
    private lateinit var grammarNoteAdapter: GrammarNoteAdapter
    private lateinit var navController: NavController

    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val grammarVM: GrammarVM by activityViewModels<GrammarVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoriteGrammarsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val favoriteGrammars = MySharedPreference.getInstance(requireActivity().application).getFavoriteGrammars(MySharedPreference.FAVORITE_GRAMMARS)
        if (favoriteGrammars.isEmpty()){
            binding.dontHaveDataMessage.visibility = VISIBLE
            binding.favoriteGrammars.visibility = INVISIBLE
        }else{
            grammarNoteAdapter = GrammarNoteAdapter(favoriteGrammars.toList())
            initViews()
        }

        initListeners()
    }

    private fun initViews() {
        binding.favoriteGrammars.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = grammarNoteAdapter
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

        if(::grammarNoteAdapter.isInitialized){
            grammarNoteAdapter.itemClicked = { grammar ->
                handleItemGrammarClicked(grammar)
            }
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack()
        mainVM.grammarsNoteClicked.value = false
        grammarVM.loadFavoriteGrammars.value = true
    }

    private fun handleItemGrammarClicked(grammar: Grammar){
        val direction = FavoriteGrammarsFragmentDirections.actionFavoriteGrammarsFragmentToGrammarFragment(grammar)
        navController.navigate(direction)
    }
}