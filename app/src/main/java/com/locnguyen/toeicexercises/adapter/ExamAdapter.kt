package com.locnguyen.toeicexercises.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemExamBinding
import com.locnguyen.toeicexercises.model.Exam

class ExamAdapter(var listExam: List<Exam> = emptyList(), var itemClicked: (Exam) -> Unit = {}) : RecyclerView.Adapter<ExamAdapter.ExamVH>(){

    class ExamVH(val binding: ItemExamBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamVH {
        return ExamVH(ItemExamBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listExam.size
    }

    override fun onBindViewHolder(holder: ExamVH, position: Int) {
        val data = listExam[position]
        val context = holder.binding.root.context

        holder.binding.title.text = data.title
        holder.binding.content.text = context.getString(R.string.Exam_content_regex, data.time, 200)
        if (data.isUnlock){
            holder.binding.icLock.visibility = INVISIBLE
        }
        else{
            holder.binding.icLock.visibility = VISIBLE
        }

        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }

}