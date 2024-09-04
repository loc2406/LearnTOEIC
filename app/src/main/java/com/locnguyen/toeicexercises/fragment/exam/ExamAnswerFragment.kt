package com.locnguyen.toeicexercises.fragment.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.ExamAnswerAdapter
import com.locnguyen.toeicexercises.databinding.ExamAnswerFragmentBinding
import com.locnguyen.toeicexercises.viewmodel.ExamVM

class ExamAnswerFragment: Fragment() {
    private lateinit var binding: ExamAnswerFragmentBinding
    private lateinit var navController: NavController
    private lateinit var examVM: ExamVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExamAnswerFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]

        initViews()
    }

    private fun initViews() {
        binding.answers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = ExamAnswerAdapter(examVM.examQuestions, examVM.userAnswers){ position, question, userAnswer ->
                val action = ExamAnswerFragmentDirections.actionExamAnswerFragmentToAnswerFragment(position, question, userAnswer)
                navController.navigate(action)
            }
        }
    }
}