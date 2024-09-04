package com.locnguyen.toeicexercises.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemTheoryBinding

class TheoryAdapter(private val listType: List<Pair<String, Int>>, private var itemClicked : (String) -> Unit): RecyclerView.Adapter<TheoryAdapter.TheoryVH>() {

    class TheoryVH(val binding: ItemTheoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheoryVH {
        return TheoryVH(ItemTheoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listType.size
    }

    override fun onBindViewHolder(holder: TheoryVH, position: Int) {
        val data = listType[position]
        val context = holder.binding.root.context

        holder.binding.content.text = data.first
        holder.binding.ic.setImageDrawable(AppCompatResources.getDrawable(context, data.second)!!)
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data.first)
        }
    }
}