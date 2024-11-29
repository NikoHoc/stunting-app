package com.dicoding.stunting.ui.main.history.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HistoryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragments = listOf(
        StuntingHistoryFragment(),
        FoodHistoryFragment()
    )

    override fun getItemCount(): Int = tabFragments.size

    override fun createFragment(position: Int): Fragment = tabFragments[position]
}