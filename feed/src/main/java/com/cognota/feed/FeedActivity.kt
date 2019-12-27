package com.cognota.feed

import android.os.Bundle
import com.cognota.core.application.CoreApp
import com.cognota.core.ui.BaseActivity
import com.cognota.feed.commons.di.DaggerFeedComponent
import com.cognota.feed.commons.di.FeedComponent
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : BaseActivity() {

    lateinit var feedComponent: FeedComponent
    private var fragmentAdapter: FeedPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val appComponent = (applicationContext as CoreApp).coreComponent
        feedComponent = DaggerFeedComponent.factory().create(appComponent)
        feedComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        setupNavigation()
    }

    private fun setupNavigation() {
        fragmentAdapter = FeedPagerAdapter(this)
        nav_viewpager.offscreenPageLimit = FeedPagerAdapter.TOTAL_FRAGMENTS - 1
        nav_viewpager.adapter = fragmentAdapter
        nav_viewpager.currentItem = 0
        nav_viewpager.isUserInputEnabled = false

        bottomBar.onItemSelected = {
            nav_viewpager.currentItem = it
        }
    }

}
