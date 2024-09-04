package com.locnguyen.toeicexercises.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemTheoryBinding
import com.locnguyen.toeicexercises.databinding.ItemWordBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.SpeakTextHelper

class WordAdapter(val words: List<Word>, private val itemClicked: (Word) -> Unit): RecyclerView.Adapter<WordAdapter.WordVH>() {

    class WordVH(val binding: ItemWordBinding): RecyclerView.ViewHolder(binding.root){
        var isFavorite: Boolean = false
            set(value) {
                field = value

                if (this.isFavorite) {
                    binding.icFavorite.setImageDrawable(
                        AppCompatResources.getDrawable(
                            binding.root.context,
                            R.drawable.ic_star
                        )
                    )
                } else {
                    binding.icFavorite.setImageDrawable(
                        AppCompatResources.getDrawable(
                            binding.root.context,
                            R.drawable.ic_star_empty
                        )
                    )
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordVH {
        return WordVH(ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordVH, position: Int) {
        val data = words[position]
        val context = holder.binding.root.context

        holder.binding.title.text = data.title
        holder.binding.pronounceValue.text = data.pronounce
        holder.binding.content.text = data.shortMean
        holder.binding.icFavorite.setOnClickListener{
            holder.isFavorite = !holder.isFavorite
        }
        holder.binding.icPronounce.setOnClickListener {
            SpeakTextHelper(context).speakText(data.title, "en")
        }
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }
}