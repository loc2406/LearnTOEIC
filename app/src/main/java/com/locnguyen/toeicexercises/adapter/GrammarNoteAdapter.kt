package com.locnguyen.toeicexercises.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.databinding.ItemGrammarNoteBinding
import com.locnguyen.toeicexercises.databinding.ItemWordNoteBinding
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.model.Word

class GrammarNoteAdapter(private var grammars: List<Grammar> = emptyList(), var itemClicked: (Grammar) -> Unit = {}) :
    RecyclerView.Adapter<GrammarNoteAdapter.GrammarNoteVH>() {
    class GrammarNoteVH(val binding: ItemGrammarNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarNoteVH {
        return GrammarNoteVH(
            ItemGrammarNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return grammars.size
    }

    override fun onBindViewHolder(holder: GrammarNoteVH, position: Int) {
        val data = grammars[position]

        holder.binding.title.text = data.title
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }

    fun updateFavoriteList(grammars: List<Grammar>){
        this.grammars = grammars
        notifyDataSetChanged()
    }
}