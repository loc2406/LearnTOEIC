package com.locnguyen.toeicexercises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemGrammarLessonBinding

class GrammarLessonAdapter(private val grammarLessons: List<Int>, private val itemClicked: (Int) -> Unit):
    RecyclerView.Adapter<GrammarLessonAdapter.GrammarLessonVH>() {

        private var currentLesson: Int = grammarLessons[0]

    class GrammarLessonVH(val binding: ItemGrammarLessonBinding): RecyclerView.ViewHolder(binding.root){
        val context: Context = binding.root.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarLessonVH {
        return GrammarLessonVH(ItemGrammarLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return grammarLessons.size
    }

    override fun onBindViewHolder(holder: GrammarLessonVH, position: Int) {
        val data = grammarLessons[position]

        if (data == currentLesson){
            holder.binding.icBook.setImageDrawable(AppCompatResources.getDrawable(holder.context, R.drawable.ic_book_open))
            holder.binding.root.background = AppCompatResources.getDrawable(holder.context, R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
        }
        else{
            holder.binding.icBook.setImageDrawable(AppCompatResources.getDrawable(holder.context, R.drawable.ic_book_close))
            holder.binding.root.background = null
        }

        holder.binding.lesson.text = holder.context.getString(R.string.Lesson_regex, data)

        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }

    fun setCurrentLesson(lesson: Int){
        currentLesson = lesson
        notifyDataSetChanged()
    }
}