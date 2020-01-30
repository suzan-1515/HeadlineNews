package com.cognota.feed.search

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.App
import com.cognota.core.di.ModuleScope
import com.cognota.core.extensions.hideKeyboard
import com.cognota.core.ui.BaseActivity
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.commons.di.SharedComponentProvider
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.search.adapter.SearchFeedController
import com.cognota.feed.search.di.SearchFeedComponent
import com.cognota.feed.search.viewmodel.SearchFeedViewModel
import com.cognota.feed.search.viewmodel.SearchFeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_feed_search.*
import timber.log.Timber
import javax.inject.Inject

@ModuleScope
class FeedSearchActivity : BaseActivity() {

    val feedComponent: SearchFeedComponent by lazy {
        SharedComponentProvider.searchComponent((applicationContext as App).coreComponent)
    }

    @Inject
    lateinit var viewModelFactory: SearchFeedViewModelFactory

    private val viewModel: SearchFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchFeedViewModel::class.java)
    }

    @Inject
    lateinit var searchFeedController: SearchFeedController

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        feedComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv.setControllerAndBuildModels(searchFeedController)

        initiateDataListener()

    }

    private fun initiateDataListener() {
//        Observe the outcome and update state of the screen accordingly
        viewModel.feeds.observe(
            this,
            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        searchFeedController.setProgress(resource.isLoading())
                    }
                    StatefulResource.State.SUCCESS -> {
                        searchFeedController.setProgress(resource.isLoading())
                        if (resource.hasData()) {
                            Timber.d("Feed data received ")
                            searchFeedController.setFeeds(resource.getData())
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                root,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
                                ),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.ok) { snackBar?.dismiss() }
                        }
                        snackBar?.show()
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        Timber.d("Network error")
                        searchFeedController.setProgress(resource.isLoading())
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                resource.message
                                    ?: R.string.no_network_connection
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    StatefulResource.State.ERROR_API -> {
                        Timber.d("Api error")
                        searchFeedController.setProgress(resource.isLoading())
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                resource.message ?: R.string.service_error
                            ), Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    else -> Timber.d("Unknown state")
                }
            })

        viewModel.tags.observe(
            this,
            Observer { data ->
                searchFeedController.setTags(data)
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.onActionViewExpanded()
        searchView.requestFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank() && query.length > 3) {
                    viewModel.getSearchFeed(query)
                    hideKeyboard()
                }
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


