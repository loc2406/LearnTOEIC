package com.locnguyen.toeicexercises.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemExerciseBinding
import com.locnguyen.toeicexercises.model.Exam

class ExerciseAdapter(private val listExam: List<Exam>, private val itemClicked: (String) -> Unit): RecyclerView.Adapter<ExerciseAdapter.ExerciseVH>() {

    class ExerciseVH(val binding: ItemExerciseBinding): RecyclerView.ViewHolder(binding.root){
        fun getImgDrawable(name: String): Drawable? {
            return when(name){
                "Chọn câu đúng" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_exercise1)!!
                "Nối câu" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_exercise2)!!
                else -> null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseVH {
        return ExerciseVH(ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listExam.size
    }

    override fun onBindViewHolder(holder: ExerciseVH, position: Int) {
        val data = listExam[position]
        val context = holder.binding.root.context

        holder.binding.content.text = data.title
        holder.binding.ic.setImageDrawable(holder.getImgDrawable(data.title))
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data.title)
        }
    }
}