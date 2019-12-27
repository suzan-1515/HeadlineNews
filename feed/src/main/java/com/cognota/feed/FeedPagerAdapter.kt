package com.cognota.feed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cognota.feed.list.ui.FeedCategoryFragment
import com.cognota.feed.list.ui.PersonalizedFeedFragment

class FeedPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    companion object {
        const val TOTAL_FRAGMENTS = 3
    }

    override fun getItemCount() =
        TOTAL_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalizedFeedFragment.newInstance()
            1 -> FeedCategoryFragment.newInstance()
            2 -> FeedCategoryFragment.newInstance()
            else -> PersonalizedFeedFragment.newInstance()
        }
    }
}