package com.cognota.feed.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cognota.feed.R
import timber.log.Timber

class FeedHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("personalised fragment: onViewCreated")

        setupNavigation()
    }

    private fun setupNavigation() {
//        nav_viewpager.adapter = FeedHomePagerAdapter(requireActivity())
//        nav_viewpager.currentItem = 0
//        nav_viewpager.isUserInputEnabled = false
//
//        bottomBar.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.menu_for_you -> nav_viewpager.currentItem = 0
//                R.id.menu_categories -> nav_viewpager.currentItem = 1
//                R.id.menu_bookmarked -> nav_viewpager.currentItem = 2
//            }
//            true
//        }
    }

}
