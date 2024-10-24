package com.locnguyen.toeicexercises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemWordBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.SpeakTextHelper

enum class FavoriteWordAction{
    ADD, REMOVE, NONE
}

class WordAdapter(var words: List<Word> = emptyList(), var favoriteWords: List<Word> = emptyList(), var itemClicked: (Word) -> Unit = {}, var pronounceWord: (String) -> Unit = {}): RecyclerView.Adapter<WordAdapter.WordVH>() {

    private var defaultList: List<Word> = words
    val favoriteWordsChange: MutableLiveData<Pair<FavoriteWordAction, Word?>> by lazy {MutableLiveData(Pair(FavoriteWordAction.NONE, null))}

    class WordVH(val binding: ItemWordBinding): RecyclerView.ViewHolder(binding.root){
        val context: Context = binding.root.context
        var isFavorite: Boolean = false

        fun checkFavoriteWord() {
            isFavorite = true

            binding.icFavorite.setImageDrawable(
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.ic_favorite
                )
            )
        }

        fun uncheckFavoriteWord(){
            isFavorite = false

            binding.icFavorite.setImageDrawable(
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.ic_unfavorite
                )
            )
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

        holder.binding.title.text = data.title
        holder.binding.pronounceValue.text = data.pronounce
        holder.binding.content.text = data.shortMean

        if (favoriteWords.contains(data)){
            holder.checkFavoriteWord()
        }
        else{
            holder.uncheckFavoriteWord()
        }

        holder.binding.icFavorite.setOnClickListener{
            holder.isFavorite = !holder.isFavorite

            if (holder.isFavorite) {
                favoriteWordsChange.value = Pair(FavoriteWordAction.ADD, data)
            } else {
                favoriteWordsChange.value = Pair(FavoriteWordAction.REMOVE, data)
            }
            notifyItemChanged(position)
        }
        holder.binding.icPronounce.setOnClickListener {
            data.title?.let{ pronounceWord.invoke(it) }
        }
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data)
        }
    }

    fun setSearchResult(result: List<Word>){
        words = result
        notifyDataSetChanged()
    }

    fun stopSearchAction(){
        words = defaultList
        notifyDataSetChanged()
    }
}