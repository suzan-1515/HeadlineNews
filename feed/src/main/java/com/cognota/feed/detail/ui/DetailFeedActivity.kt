package com.cognota.feed.detail.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.cognota.core.App
import com.cognota.feed.R
import com.cognota.feed.commons.di.SharedComponentProvider
import com.cognota.feed.detail.di.DetailFeedComponent

class DetailFeedActivity : AppCompatActivity() {

    val feedComponent: DetailFeedComponent by lazy {
        SharedComponentProvider.detailsComponent((applicationContext as App).coreComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        feedComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed_detail)

        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.detail, intent.extras)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onDestroy() {
        SharedComponentProvider.destroyDetailsComponent()
        super.onDestroy()
    }

}
