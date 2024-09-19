package com.locnguyen.toeicexercises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.databinding.ItemGrammarBinding
import com.locnguyen.toeicexercises.model.Grammar

class GrammarAdapter(private val grammars: List<Grammar>, var itemClicked: (Grammar) -> Unit = {}):
    RecyclerView.Adapter<GrammarAdapter.GrammarVH>() {

        private var currentLesson: Int = 1

    class GrammarVH(val binding: ItemGrammarBinding): RecyclerView.ViewHolder(binding.root){
        val context: Context = binding.root.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarVH {
        return GrammarVH(ItemGrammarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return grammars.filter{grammar -> grammar.level == currentLesson}.size
    }

    override fun onBindViewHolder(holder: GrammarVH, position: Int) {
        val data = grammars.filter{grammar -> grammar.level == currentLesson}[position]

        holder.binding.content.text = data.title

        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }

    private fun resetView(holder: GrammarVH) {
        holder.binding.root.visibility = VISIBLE
    }

    fun setData(lesson: Int) {
        currentLesson = lesson
        notifyDataSetChanged()
    }
}