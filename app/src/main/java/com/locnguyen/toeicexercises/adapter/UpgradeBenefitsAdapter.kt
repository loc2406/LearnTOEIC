package com.locnguyen.toeicexercises.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ItemUpgradeBenefitBinding

class UpgradeBenefitsAdapter(private val benefits: List<Pair<String, String>>) : RecyclerView.Adapter<UpgradeBenefitsAdapter.UpgradeBenefitsVH>(){

    class UpgradeBenefitsVH(val binding: ItemUpgradeBenefitBinding): RecyclerView.ViewHolder(binding.root) {
        fun getImgBenefit(title: String): Drawable? {
            return when(title){
                "Mở khóa bài học" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_unlock)
                "Không quảng cáo" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_no_ads)
                "Học ngoại tuyến" -> AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_no_wifi)
                else -> null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpgradeBenefitsVH {
        return UpgradeBenefitsVH(ItemUpgradeBenefitBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return benefits.size
    }

    override fun onBindViewHolder(holder: UpgradeBenefitsVH, position: Int) {
        val data = benefits[position]
        holder.binding.icBenefit.setImageDrawable(holder.getImgBenefit(data.first))
        holder.binding.benefitTitle.text = data.first
        holder.binding.benefitContent.text = data.second
    }
}