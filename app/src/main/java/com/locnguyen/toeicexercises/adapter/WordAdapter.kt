package com.locnguyen.toeicexercises.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.databinding.ItemWordBinding
import com.locnguyen.toeicexercises.model.Word


class WordAdapter(
    private var words: List<Word>,
    var itemClicked: (Word) -> Unit = {},
    var pronounceWord: (String) -> Unit = {}
) : RecyclerView.Adapter<WordAdapter.WordVH>() {

    private var defaultList: List<Word> = words

    class WordVH(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordVH {
        return WordVH(ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordVH, position: Int) {
        val data = words[position]

        holder.binding.title.text = data.title
        holder.binding.pronounceValue.text = data.pronounce
        holder.binding.content.text = data.shortMean

        holder.binding.icPronounce.setOnClickListener {
            data.title?.let { pronounceWord.invoke(it) }
        }
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }

    fun setCurrentList(list: List<Word>) {
        words = list
        notifyDataSetChanged()
    }

    fun stopSearchAction() {
        words = defaultList
        notifyDataSetChanged()
    }
}