package com.locnguyen.toeicexercises.fragment.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.toeicexercises.adapter.ExamAnswerAdapter
import com.locnguyen.toeicexercises.databinding.ListAnswersFragmentBinding
import com.locnguyen.toeicexercises.viewmodel.ExamVM

class ListAnswersFragment: Fragment() {
    private lateinit var binding: ListAnswersFragmentBinding
    private lateinit var navController: NavController
    private lateinit var examVM: ExamVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListAnswersFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]

        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.answers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = ExamAnswerAdapter(examVM.examQuestions, examVM.userAnswers){ position, question, userAnswer, isShowAnswer ->
                val action = ListAnswersFragmentDirections.actionListAnswersFragmentToAnswerFragment(position, question, userAnswer, isShowAnswer)
                navController.navigate(action)
            }
        }
    }

    private fun initListeners(){
        binding.icBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}