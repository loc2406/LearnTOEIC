package com.locnguyen.toeicexercises.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.fragment.exam.QuestionDetailFragment
import com.locnguyen.toeicexercises.model.ExamPart
import com.locnguyen.toeicexercises.model.ExamPartContent
import com.locnguyen.toeicexercises.model.Question

class ExamQuestionsAdapter(private val fm: FragmentActivity, private var allQuestions: List<Pair<String, Question>>): FragmentStateAdapter(fm){

    override fun getItemCount(): Int {
        return allQuestions.size
    }

    override fun createFragment(position: Int): Fragment {
        return QuestionDetailFragment().apply {
            val isShowAnswer: Boolean = when{
                allQuestions[position].first.contains("Picture Description", true) ||
                        allQuestions[position].first.contains("Question Response", true) -> false
                else -> true
            }

            arguments = Bundle().apply {
                putParcelable("QUESTION", allQuestions[position].second)
                putInt("POSITION", position)
                putBoolean("IS_SHOW_ANSWER", isShowAnswer)
            }
        }
    }

    fun removeAllQuestions(){
        allQuestions = emptyList()
        notifyDataSetChanged()
    }
}