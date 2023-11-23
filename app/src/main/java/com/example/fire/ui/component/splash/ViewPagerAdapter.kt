package com.example.fire.ui.component.splash

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fire.data.dto.iot.FragmentData

class ViewPagerAdapter (fragmentActivity: FragmentActivity, private val fragmentList: List<FragmentData>)
    : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].fragment ?: Fragment()
    }
}