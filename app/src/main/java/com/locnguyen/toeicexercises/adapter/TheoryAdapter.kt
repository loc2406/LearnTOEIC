package com.locnguyen.toeicexercises.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemPracticeBinding

class TheoryAdapter(private val types: List<String>, var itemClicked : (String) -> Unit = {}): RecyclerView.Adapter<TheoryAdapter.TheoryVH>() {

    class TheoryVH(val binding: ItemPracticeBinding): RecyclerView.ViewHolder(binding.root){
        fun getImgDrawable(name: String): Drawable? {
            return when(name){
                "Từ vựng" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_word)!!
                "Ngữ pháp" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_grammar)!!
                else -> null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheoryVH {
        return TheoryVH(ItemPracticeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: TheoryVH, position: Int) {
        val data = types[position]

        holder.binding.content.text = data
        holder.binding.ic.setImageDrawable(holder.getImgDrawable(data))
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }
}