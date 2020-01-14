package com.cognota.feed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cognota.feed.category.ui.CategoriesFeedFragment
import com.cognota.feed.personalised.ui.PersonalizedFeedFragment

class FeedPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    companion object {
        const val TOTAL_FRAGMENTS = 2
    }

    override fun getItemCount() =
        TOTAL_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalizedFeedFragment.newInstance()
            1 -> CategoriesFeedFragment.newInstance()
            else -> PersonalizedFeedFragment.newInstance()
        }
    }
}