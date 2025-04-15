package com.locnguyen.toeicexercises.fragment.main

import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.ExamQuestionsAdapter
import com.locnguyen.toeicexercises.databinding.ExerciseFragmentBinding
import com.locnguyen.toeicexercises.model.Question
import com.locnguyen.toeicexercises.viewmodel.main.ExerciseVM
import com.locnguyen.toeicexercises.viewmodel.main.MainVM

class ExerciseFragment : Fragment() {
    private lateinit var binding: ExerciseFragmentBinding
    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val exerciseVM: ExerciseVM by activityViewModels<ExerciseVM>()
    private lateinit var navController: NavController
    private lateinit var examQuestionAdapter: ExamQuestionsAdapter
    private lateinit var allQuestions: List<Pair<String, Question>>
    private val args: ExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.exerciseVM = exerciseVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        binding.title.text = args.type
    }

    private fun handleQuestionChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            binding.orderQuestion.text = requireContext().getString(
                R.string.Order_question_in_list_question_regex,
                position + 1,
                exerciseVM.exerciseQuestions.size
            )
            exerciseVM.currentQuestion.value = position
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

        binding.icPreviousQuestion.setOnClickListener {
            exerciseVM.currentQuestion.value = exerciseVM.currentQuestion.value!! - 1
        }

        binding.icNextQuestion.setOnClickListener {
            exerciseVM.currentQuestion.value = exerciseVM.currentQuestion.value!! + 1
        }
    }

    private fun initObserves() {
        exerciseVM.exams.observe(viewLifecycleOwner){ exams ->
            exams?.let{
                allQuestions = exerciseVM.getQuestions(requireContext(), args.type)
                exerciseVM.exerciseQuestions = allQuestions as ArrayList<Pair<String, Question>>

                examQuestionAdapter = ExamQuestionsAdapter(requireActivity(), allQuestions, type = "EXERCISE")
                binding.questions.apply {
                    adapter = examQuestionAdapter
                    registerOnPageChangeCallback(handleQuestionChangeCallback())
                }
            }
        }
        exerciseVM.currentQuestion.observe(viewLifecycleOwner) { value ->
            binding.questions.currentItem = value ?: 0
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack()
        binding.questions.apply {
            adapter = null
            unregisterOnPageChangeCallback(handleQuestionChangeCallback())
        }
        mainVM.itemExamClicked.value = null
        exerciseVM.apply {
            currentQuestion.value = 0
            exerciseQuestions.clear()
            userAnswers.clear()
            listenCorrectAnswers = emptyList()
            readCorrectAnswers = emptyList()
            answerCorrect.clear()
        }
    }
}