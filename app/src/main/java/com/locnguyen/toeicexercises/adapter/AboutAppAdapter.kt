package com.locnguyen.toeicexercises.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.locnguyen.toeicexercises.fragment.AboutAppFragment1

class AboutAppAdapter(fm: FragmentActivity): FragmentStateAdapter(fm) {

    private val aboutApp1: AboutAppFragment1 by lazy {AboutAppFragment1()}

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> aboutApp1
            else -> aboutApp1
        }
    }
}