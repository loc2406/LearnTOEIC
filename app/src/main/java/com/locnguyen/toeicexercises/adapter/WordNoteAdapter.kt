package com.locnguyen.toeicexercises.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.databinding.ItemWordNoteBinding
import com.locnguyen.toeicexercises.model.Word

class WordNoteAdapter(var words: List<Word> = emptyList(), var pronounceClicked: (String) -> Unit = {}, var itemClicked: (Word) -> Unit = {}) :
    RecyclerView.Adapter<WordNoteAdapter.WordNoteVH>() {
    class WordNoteVH(val binding: ItemWordNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordNoteVH {
        return WordNoteVH(
            ItemWordNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordNoteVH, position: Int) {
        val data = words[position]

        holder.binding.apply {
            title.text = data.title
            pronounce.text = data.pronounce
            mean.text = data.shortMean

            icPronounce.setOnClickListener {
                data.title?.let { pronounceClicked.invoke(it) }
            }

            root.setOnClickListener{
                itemClicked.invoke(data)
            }
        }
    }
}