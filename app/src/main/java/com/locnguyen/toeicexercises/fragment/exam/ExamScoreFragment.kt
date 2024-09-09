package com.locnguyen.toeicexercises.fragment.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ExamScoreFragmentBinding
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import com.locnguyen.toeicexercises.viewmodel.MainVM
import java.util.Locale

class ExamScoreFragment : Fragment() {
    private lateinit var binding: ExamScoreFragmentBinding
    private lateinit var navController: NavController
    private lateinit var mainVM: MainVM
    private lateinit var examVM: ExamVM


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExamScoreFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]

        val examScore = examVM.getExamScore()

        initViews(examScore)
        initListeners()
    }

    private fun initViews(examScore: Int) {
        when (examScore) {
            in 10..400 -> binding.scoreValue.setTextColor(requireContext().getColor(R.color.red))
            in 405..780 -> binding.scoreValue.setTextColor(requireContext().getColor(R.color.orange))
            else /* in 785..990*/ -> binding.scoreValue.setTextColor(requireContext().getColor(R.color.green))
        }

        binding.scoreValue.text = examScore.toString()

        initListenChart()
        initReadChart()
    }

    private fun initListenChart() {
        val correctPercents: Int = examVM.listenCorrectAnswers.size

        binding.listeningPercent.text = requireContext().getString(R.string.Percent, correctPercents)
        binding.listeningTrueAnswer.text = requireContext().getString(
            R.string.Answer_correct_regex,
            correctPercents
        )
        binding.listeningFalseAnswer.text = requireContext().getString(
            R.string.Answer_incorrect_regex,
            100 - correctPercents
        )
        binding.listeningChart.setProgressWithAnimation(correctPercents.toFloat(), 1000)
    }

    private fun initReadChart() {
        val correctPercents: Int = examVM.readCorrectAnswers.size

        binding.readingPercent.text = requireContext().getString(R.string.Percent, correctPercents)
        binding.readingTrueAnswer.text = requireContext().getString(
            R.string.Answer_correct_regex,
            correctPercents
        )
        binding.readingFalseAnswer.text = requireContext().getString(
            R.string.Answer_incorrect_regex,
            100 - correctPercents
        )
        binding.readingChart.setProgressWithAnimation(correctPercents.toFloat(), 1000)
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handlePressedBack()
                }
            })

        binding.backMainScreen.setOnClickListener {
            handlePressedBack()
        }

        binding.answersDetail.setOnClickListener {
            navController.navigate(R.id.action_examScoreFragment_to_listAnswersFragment)
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack(R.id.mainFragment, false)
        resetExamData()
    }

    private fun resetExamData() {
        mainVM.itemExamClicked.value = null
        examVM.apply {
            exam.value = null
            examQuestions.clear()
            userAnswers.clear()
            listenCorrectAnswers = emptyList()
            readCorrectAnswers = emptyList()
            answerCorrect.clear()
        }
    }
}