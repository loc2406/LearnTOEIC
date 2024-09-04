package com.locnguyen.toeicexercises.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemNoteBinding
import com.locnguyen.toeicexercises.databinding.ItemTheoryBinding

class NoteAdapter(private val listNote: List<Pair<String, Int>>, private var itemClicked : (String) -> Unit): RecyclerView.Adapter<NoteAdapter.NoteVH>() {

    class NoteVH(val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        return NoteVH(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        val data = listNote[position]
        val context = holder.binding.root.context

        holder.binding.title.text = data.first
        holder.binding.img.setImageDrawable(AppCompatResources.getDrawable(context, data.second)!!)
        holder.binding.value.text = 100.toString()
        holder.binding.root.setOnClickListener {
            itemClicked.invoke(data.first)
        }
    }
}