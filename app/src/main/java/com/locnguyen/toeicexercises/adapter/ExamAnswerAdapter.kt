package com.locnguyen.toeicexercises.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemExamAnswerBinding
import com.locnguyen.toeicexercises.model.Question

class ExamAnswerAdapter(
    private val questions: ArrayList<Pair<String, Question>>,
    private val userAnswers: HashMap<Int, String>,
    private val itemClicked: (Int, Question, String?, Boolean) -> Unit
) :
    RecyclerView.Adapter<ExamAnswerAdapter.ExamAnswerVH>() {
    class ExamAnswerVH(val binding: ItemExamAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamAnswerVH {
        return ExamAnswerVH(
            ItemExamAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: ExamAnswerVH, position: Int) {
        setDefaultView(holder)

        val question = questions[position].second
        val userAnswer = userAnswers[position]
        val trueAnswerIndex = question.answers.indexOf(question.trueAnswer)
        val userAnswerIndex = question.answers.indexOf(userAnswer)

        holder.binding.orderQuestion.text =
            holder.context.getString(R.string.Order_question_regex, position + 1)
        var answerCharViews = listOf(
            holder.binding.answerA,
            holder.binding.answerB,
            holder.binding.answerC,
            holder.binding.answerD
        )

        for (orderAns in question.answers.indices) {
            answerCharViews[orderAns].visibility = VISIBLE
        }

        answerCharViews = answerCharViews.filter { view -> view.visibility == VISIBLE }

        if (userAnswer != null && userAnswerIndex != -1) {
            if (trueAnswerIndex == userAnswerIndex) {
                answerCharViews[trueAnswerIndex].apply {
                    setTextColor(Color.WHITE)
                    background = AppCompatResources.getDrawable(context, R.drawable.bg_circle_true_answer)
                }
            } else {
                answerCharViews[userAnswerIndex].apply {
                    setTextColor(Color.WHITE)
                    background = AppCompatResources.getDrawable(context, R.drawable.bg_circle_false_answer)
                }

                answerCharViews[trueAnswerIndex].apply {
                    setTextColor(Color.WHITE)
                    background = AppCompatResources.getDrawable(context, R.drawable.bg_circle_true_answer)
                }
            }
        } else {
            answerCharViews[trueAnswerIndex].apply {
                setTextColor(Color.WHITE)
                background = AppCompatResources.getDrawable(context, R.drawable.bg_circle_true_answer)
            }
        }

        holder.binding.root.setOnClickListener {
            itemClicked.invoke(position, question, userAnswer, isShowAnswer(questions[position].first))
        }
    }

    private fun isShowAnswer(part:String): Boolean {
        return when{
            part.contains("Picture Description", true) ||
                    part.contains("Question Response", true) -> false
            else -> true
        }
    }

    private fun setDefaultView(holder: ExamAnswerVH) {
        holder.binding.apply {
            answerA.apply {
                visibility = INVISIBLE
                setTextColor(Color.BLACK)
                background = null
            }

            answerB.apply {
                visibility = INVISIBLE
                setTextColor(Color.BLACK)
                background = null
            }

            answerC.apply {
                visibility = INVISIBLE
                setTextColor(Color.BLACK)
                background = null
            }

            answerD.apply {
                visibility = INVISIBLE
                setTextColor(Color.BLACK)
                background = null
            }
        }
    }
}