package com.locnguyen.toeicexercises.fragment.exam

import android.graphics.Color.WHITE
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.AnswerFragmentBinding
import com.locnguyen.toeicexercises.model.Question
import com.locnguyen.toeicexercises.utils.GlobalHelper
import com.locnguyen.toeicexercises.utils.dpToPx
import com.locnguyen.toeicexercises.utils.dpToPx
import com.locnguyen.toeicexercises.utils.loadImg
import kotlin.properties.Delegates

class AnswerFragment: Fragment() {
    private lateinit var binding: AnswerFragmentBinding
    private var quesPosition by Delegates.notNull<Int>()
    private lateinit var question: Question
    private var userAnswer: String? = null
    private lateinit var answerViews: List<TextView>
    private var isShowAnswer by Delegates.notNull<Boolean>()
    private lateinit var navController: NavController

    private val args by navArgs<AnswerFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AnswerFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        quesPosition = args.questionPosition
        question = args.question
        userAnswer = args.userAnswer
        isShowAnswer = args.isShowAnswer

        initViews()
        initListeners()
    }

    private fun initViews() {
        answerViews = listOf(
            binding.firstAnswer,
            binding.secondAnswer,
            binding.thirdAnswer,
            binding.fourthAnswer
        )

        initAnswerViews()
        initAnswerContents()

        binding.apply {
            headerTitle.text = requireContext().getString(R.string.Order_question, quesPosition + 1)
            title.text = question.title

            if (question.img.isNotEmpty()){
                requireContext().loadImg(question.img, img)
            }
            else{
                img.visibility = GONE
            }

            if (question.explain.isNotEmpty()){
                explainContent.text = question.explain
            }
            else{
                explainTitle.visibility = GONE
                explainContent.visibility = GONE
            }

            if (question.key.isNotEmpty()){
                keyContent.text = question.key
            }
            else{
                keyTitle.visibility = GONE
                keyContent.visibility = GONE
            }
        }

        handleShowAnswer()
    }

    private fun initAnswerViews() {
        if (isShowAnswer){
            binding.firstAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.img.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 20.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
            binding.secondAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.firstAnswer.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 10.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
            binding.thirdAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.secondAnswer.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 10.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
            binding.fourthAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.thirdAnswer.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 10.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
        }
        else{
            binding.firstAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToBottom = binding.img.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToStart = binding.secondAnswer.id
                    topMargin = 20.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER
            }
            binding.secondAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToTop = binding.firstAnswer.id
                    startToEnd = binding.firstAnswer.id
                    endToStart = binding.thirdAnswer.id
                    bottomToBottom = binding.firstAnswer.id
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER

            }
            binding.thirdAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToTop = binding.secondAnswer.id
                    startToEnd = binding.secondAnswer.id
                    endToStart = binding.fourthAnswer.id
                    bottomToBottom = binding.secondAnswer.id
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER
            }
            binding.fourthAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToTop = binding.thirdAnswer.id
                    startToEnd = binding.thirdAnswer.id
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToBottom = binding.thirdAnswer.id
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER
            }
        }
    }

    private fun initAnswerContents() {
        val answersChar = listOf("A", "B", "C", "D")

        for (i in question.answers.indices) {
            answerViews[i].apply {
                text = if (isShowAnswer){
                    requireContext().getString(
                        R.string.Question_answer_with_content_regex,
                        answersChar[i],
                        question.answers[i]
                    )
                }else{
                    requireContext().getString(
                        R.string.Question_answer_no_content_regex,
                        answersChar[i]
                    )
                }

                visibility = VISIBLE
            }
        }

        answerViews = answerViews.filter { view -> view.visibility == VISIBLE }
    }

    private fun initListeners(){
        binding.icBack.setOnClickListener {
            handlePressedBack()
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack()
    }

    private fun handleShowAnswer() {
        try {
            val userAnswerPosition = question.answers.indexOf(userAnswer ?: "")
            val trueAnswerPosition = question.answers.indexOf(question.trueAnswer)

            if (userAnswer != null){
                if (userAnswer != question.trueAnswer){
                    setFalseAnswer(answerViews[userAnswerPosition])
                    setTrueAnswer(answerViews[trueAnswerPosition])
                }else{
                    setTrueAnswer(answerViews[userAnswerPosition])
                }
            }
            else{
                setTrueAnswer(answerViews[trueAnswerPosition])
            }
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }
    }

    private fun setFalseAnswer(view: TextView) {
        view.apply {
            setTextColor(WHITE)
            background = if (isShowAnswer) {
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_second_primary_rectangle_no_stroke_10dp_corners
                )
            } else {
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_circle_false_answer
                )
            }
        }
    }

    private fun setTrueAnswer(view: TextView) {
        view.apply {
            setTextColor(WHITE)
            background = if (isShowAnswer){
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_primary_rectangle_no_stroke_10dp_corners
                )
            }
            else{
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_circle_true_answer
                )
            }
        }
    }
}