package com.locnguyen.toeicexercises.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemEvaluateBinding
import com.locnguyen.toeicexercises.model.Evaluate
import com.locnguyen.toeicexercises.utils.loadImg

class UpgradeEvaluatesAdapter(private val evaluates: List<Evaluate>) :
    RecyclerView.Adapter<UpgradeEvaluatesAdapter.UpgradeEvaluatesVH>() {
    class UpgradeEvaluatesVH(val binding: ItemEvaluateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpgradeEvaluatesVH {
        return UpgradeEvaluatesVH(
            ItemEvaluateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return evaluates.size
    }

    override fun onBindViewHolder(holder: UpgradeEvaluatesVH, position: Int) {
        val data = evaluates[position]
        val context = holder.binding.root.context

        holder.binding.apply {
            context.loadImg(
                AppCompatResources.getDrawable(context, R.drawable.ic_user)!!,
                img
            )
            name.text = data.name
            time.text = data.time
            evaluate.text = data.content
            starValue.text = data.stars.toString()
        }
    }
}