package com.cognota.feed

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.cognota.core.application.CoreApp
import com.cognota.core.di.ModuleScope
import com.cognota.core.extensions.setupWithNavController
import com.cognota.core.ui.BaseActivity
import com.cognota.feed.category.di.CategoryFeedComponent
import com.cognota.feed.commons.di.FeedComponent
import com.cognota.feed.commons.di.SharedComponentProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

@ModuleScope
class FeedActivity : BaseActivity() {

    val feedComponent: FeedComponent by lazy {
        SharedComponentProvider.feedComponent((applicationContext as CoreApp).coreComponent)
    }
    var categoryFeedComponent: CategoryFeedComponent? = null
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        feedComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomBar)

        val navGraphIds =
            listOf(R.navigation.personalised, R.navigation.category, R.navigation.bookmark)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onDestroy() {
        categoryFeedComponent = null
        SharedComponentProvider.destroyFeedComponent()
        super.onDestroy()
    }

}
