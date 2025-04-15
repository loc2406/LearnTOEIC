package com.locnguyen.toeicexercises.fragment.word

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.WordAdapter
import com.locnguyen.toeicexercises.databinding.ListWordFragmentBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.Event
import com.locnguyen.toeicexercises.utils.SpeakTextHelper
import com.locnguyen.toeicexercises.utils.isHasNetWork
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.main.MainVM
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
        wordAdapter = WordAdapter(wordVM.words.value!!)

        initViews()
        initListeners()
        initObserves()

        if (loadingDialog.isShowing) loadingDialog.dismiss()
    }

    private fun initViews() {
        binding.words.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
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
                        wordVM.searchFilteredList.value = wordVM.words.value
                    } else {
                        wordVM.handleSearchChange(it)
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
        wordVM.levelFilteredList.observe(viewLifecycleOwner) { list ->
            list?.let {
                wordAdapter.updateCurrentList(levelFilteredList = it)
            }
        }

        wordVM.searchFilteredList.observe(viewLifecycleOwner) { list ->
            list?.let {
                wordAdapter.updateCurrentList(searchFilteredList = it)
            }
        }

        wordVM.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                requireContext().toastMessage(it)
                wordVM.message.value = null
            }
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack()
        wordVM.isNeedLoaded.value = Event(true)
    }

    private fun handleItemWordClick(word: Word) {
        val action = ListWordFragmentDirections.actionListWordFragmentToWordFragment(word)
        navController.navigate(action)
    }
}