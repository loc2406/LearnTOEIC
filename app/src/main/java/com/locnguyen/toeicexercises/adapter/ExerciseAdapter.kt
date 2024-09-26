package com.locnguyen.toeicexercises.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemPracticeBinding
import com.locnguyen.toeicexercises.model.Exam

class ExerciseAdapter(private val types: List<String>, var itemClicked: (String) -> Unit = {}): RecyclerView.Adapter<ExerciseAdapter.ExerciseVH>() {

    class ExerciseVH(val binding: ItemPracticeBinding): RecyclerView.ViewHolder(binding.root){
        fun getImgDrawable(name: String): Drawable? {
            return when(name){
                "Chọn câu đúng" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_select_correct_answer)!!
                "Nối câu" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_connect_sentence)!!
                else -> null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseVH {
        return ExerciseVH(ItemPracticeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: ExerciseVH, position: Int) {
        val data = types[position]

        holder.binding.content.text = data
        holder.binding.ic.setImageDrawable(holder.getImgDrawable(data))
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }
}