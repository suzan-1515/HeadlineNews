package com.cognota.feed.search

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.cognota.core.App
import com.cognota.core.di.ModuleScope
import com.cognota.core.extensions.hideKeyboard
import com.cognota.core.ui.BaseActivity
import com.cognota.feed.R
import com.cognota.feed.commons.di.SharedComponentProvider
import com.cognota.feed.search.di.SearchFeedComponent
import kotlinx.android.synthetic.main.activity_feed_search.*

@ModuleScope
class FeedSearchActivity : BaseActivity() {

    val feedComponent: SearchFeedComponent by lazy {
        SharedComponentProvider.searchComponent((applicationContext as App).coreComponent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        feedComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.onActionViewExpanded()
        searchView.requestFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@FeedSearchActivity, "Query: $query", Toast.LENGTH_SHORT)
                    .show()
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        SharedComponentProvider.destroySearchComponent()
        super.onDestroy()
    }

}


