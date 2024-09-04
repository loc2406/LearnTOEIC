package com.locnguyen.toeicexercises.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.MainActivity
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.ExerciseAdapter
import com.locnguyen.toeicexercises.adapter.NoteAdapter
import com.locnguyen.toeicexercises.adapter.TheoryAdapter
import com.locnguyen.toeicexercises.databinding.PracticeFragmentBinding
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.viewmodel.MainVM

class PracticeFragment: Fragment() {

    private lateinit var binding: PracticeFragmentBinding
    private lateinit var theoryAdapter: TheoryAdapter
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var mainVM: MainVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PracticeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            requireActivity().requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), MainActivity.CODE)
        }

        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        theoryAdapter = TheoryAdapter(listOf(Pair("Từ vựng", R.drawable.ic_theory1), Pair("Ngữ pháp", R.drawable.ic_theory2))){ typeName ->
            mainVM.itemTheoryClicked.value = typeName
        }
        exerciseAdapter = ExerciseAdapter(listOf(Exam("Chọn câu đúng"), Exam("Nối câu"))){ exerciseName ->
            mainVM.itemExerciseClicked.value = exerciseName
        }
        noteAdapter = NoteAdapter(listOf(Pair("Từ vựng", R.drawable.ic_theory1), Pair("Ngữ pháp", R.drawable.ic_theory2))) { noteName ->

        }

        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.theoryContent.apply {
            adapter = theoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        binding.exerciseContent.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        binding.noteContent.apply {
            adapter = noteAdapter
            layoutManager = object: LinearLayoutManager(requireContext()){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    private fun initListeners(){
    }
}