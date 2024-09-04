package com.locnguyen.toeicexercises.fragment.exam

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.AnswerFragmentBinding
import com.locnguyen.toeicexercises.model.Question
import com.locnguyen.toeicexercises.utils.GlobalHelper
import kotlin.properties.Delegates

class AnswerFragment: Fragment() {
    private lateinit var binding: AnswerFragmentBinding
    private var quesPosition by Delegates.notNull<Int>()
    private lateinit var question: Question
    private var userAnswer: String? = null
    private lateinit var answerViews: List<TextView>

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

        quesPosition = args.questionPosition
        question = args.question
        userAnswer = args.userAnswer

        initViews()
    }

    private fun initViews() {
        binding.apply {
            headerTitle.text = requireContext().getString(R.string.Order_question, quesPosition + 1)
            title.text = question.title

            if (question.img.isNotEmpty()){
                GlobalHelper(requireContext()).loadImg(question.img, img)
            }
            else{
                img.visibility = GONE
            }

            answerViews = listOf(binding.firstAnswer, binding.secondAnswer, binding.thirdAnswer, binding.fourthAnswer)
            for (i in question.answers.indices){
                answerViews[i].text = question.answers[i]
                answerViews[i].visibility = VISIBLE
            }
            answerViews = answerViews.filter { view -> view.visibility == VISIBLE }

            if (question.explain.isNotEmpty()){
                explainContent.text = Html.fromHtml(question.explain, Html.FROM_HTML_MODE_LEGACY)
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

    private fun handleShowAnswer() {
        if (userAnswer != null){
            if (userAnswer != question.trueAnswer){
                setFalseAnswer(userAnswer!!)
                setTrueAnswer(question.trueAnswer)
            }else{
                setTrueAnswer(userAnswer!!)
            }
        }
        else{
            setTrueAnswer(question.trueAnswer)
        }
    }

    private fun setFalseAnswer(answer: String) {
        answerViews.forEach { view ->
            if (view.text == answer){
                view.setTextColor(android.graphics.Color.WHITE)
                view.background = ResourcesCompat.getDrawable(requireContext().resources, R.drawable.bg_second_primary_rectangle_no_stroke_10dp_corners, null)
            }
        }
    }

    private fun setTrueAnswer(answer: String) {
        answerViews.forEach { view ->
            if (view.text == answer){
                view.setTextColor(android.graphics.Color.WHITE)
                view.background = ResourcesCompat.getDrawable(requireContext().resources, R.drawable.bg_primary_rectangle_no_stroke_10dp_corners, null)
            }
        }
    }
}