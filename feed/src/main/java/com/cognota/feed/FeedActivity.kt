package com.cognota.feed

import android.os.Bundle
import com.cognota.core.application.CoreApp
import com.cognota.core.di.ModuleScope
import com.cognota.core.ui.BaseActivity
import com.cognota.feed.commons.di.DaggerFeedComponent
import com.cognota.feed.commons.di.FeedComponent
import com.cognota.feed.list.di.CategoryFeedComponent
import kotlinx.android.synthetic.main.activity_feed.*

@ModuleScope
class FeedActivity : BaseActivity() {

    lateinit var feedComponent: FeedComponent
    var categoryFeedComponent: CategoryFeedComponent? = null
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
        nav_viewpager.adapter = fragmentAdapter
        nav_viewpager.currentItem = 0
        nav_viewpager.isUserInputEnabled = false

        bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_for_you -> nav_viewpager.currentItem = 0
                R.id.menu_categories -> nav_viewpager.currentItem = 1
                R.id.menu_setting -> nav_viewpager.currentItem = 2
            }
            true
        }
    }

}
