package com.cognota.feed.search.ui

import android.os.Bundle
import androidx.navigation.findNavController
import com.cognota.core.App
import com.cognota.core.di.ModuleScope
import com.cognota.core.ui.BaseActivity
import com.cognota.feed.R
import com.cognota.feed.commons.di.SharedComponentProvider
import com.cognota.feed.search.di.SearchFeedComponent
import kotlinx.android.synthetic.main.activity_feed_search.*

@ModuleScope
class SearchFeedActivity : BaseActivity() {

    val feedComponent: SearchFeedComponent by lazy {
        SharedComponentProvider.searchComponent((applicationContext as App).coreComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        feedComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val findNavController = findNavController(R.id.nav_host_fragment)
        findNavController.setGraph(R.navigation.search, intent.extras)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onDestroy() {
        SharedComponentProvider.destroySearchComponent()
        super.onDestroy()
    }

}


