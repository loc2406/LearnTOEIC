package com.locnguyen.toeicexercises.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.locnguyen.toeicexercises.fragment.exam.ListExamFragment
import com.locnguyen.toeicexercises.fragment.main.PracticeFragment
import com.locnguyen.toeicexercises.fragment.main.SettingFragment
import com.locnguyen.toeicexercises.fragment.main.UpgradeFragment

class BottomNavAdapter(fm: FragmentActivity): FragmentStateAdapter(fm) {

    private val practiceFragment: PracticeFragment by lazy { PracticeFragment() }
    private val examFragment: ListExamFragment by lazy { ListExamFragment() }
    private val upgradeFragment: UpgradeFragment by lazy { UpgradeFragment() }
    private val settingFragment: SettingFragment by lazy { SettingFragment() }

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> practiceFragment
            1-> examFragment
            2-> upgradeFragment
            3-> settingFragment
            else -> Fragment()
        }
    }
}