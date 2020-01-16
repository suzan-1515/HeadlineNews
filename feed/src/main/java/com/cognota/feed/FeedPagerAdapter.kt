package com.cognota.feed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cognota.feed.category.ui.CategoriesFeedFragment
import com.cognota.feed.personalised.ui.PersonalisedFeedFragment
import com.cognota.feed.saved.SavedFeedFragment

class FeedPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    companion object {
        const val TOTAL_FRAGMENTS = 3
    }

    override fun getItemCount() =
        TOTAL_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalisedFeedFragment.newInstance()
            1 -> CategoriesFeedFragment.newInstance()
            2 -> SavedFeedFragment.newInstance()
            else -> PersonalisedFeedFragment.newInstance()
        }
    }
}