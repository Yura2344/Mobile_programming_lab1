package com.example.lab1clock

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter: FragmentStateAdapter {

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ClockFragment()
            1 -> TimerFragment()
            2 -> SettingsFragment()
            else -> ClockFragment()
        }

    }
}