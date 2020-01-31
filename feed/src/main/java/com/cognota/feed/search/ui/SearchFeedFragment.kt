package com.cognota.feed.search.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.cognota.core.di.FeatureScope
import com.cognota.core.extensions.hideKeyboard
import com.cognota.core.ui.BaseFragment
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.search.adapter.SearchFeedController
import com.cognota.feed.search.viewmodel.SearchFeedViewModel
import com.cognota.feed.search.viewmodel.SearchFeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed_detail.*
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@FeatureScope
class SearchFeedFragment : BaseFragment() {

    private val args: SearchFeedFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: SearchFeedViewModelFactory

    private val viewModel: SearchFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchFeedViewModel::class.java)
    }

    @Inject
    lateinit var searchFeedController: SearchFeedController

    private var snackBar: Snackbar? = null
    private lateinit var searchView: SearchView

    override fun onAttach(context: Context) {
        Timber.d("attaching search feed fragment")
        (activity as SearchFeedActivity).feedComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initiateDataListener()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_feed_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")

        rv.setControllerAndBuildModels(searchFeedController)
        searchFeedController.topicClickAction = {
            if (::searchView.isInitialized)
                searchView.setQuery(it, true)
        }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Timber.d("onCreateOptionsMenu")
        inflater.inflate(R.menu.search, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.onActionViewExpanded()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank() && query.length > 3) {
                    viewModel.getSearchFeed(query)
                }
                hideKeyboard()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        args.query?.let {
            searchView.setQuery(it, true)
        }
        return super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance(): SearchFeedFragment {
            return SearchFeedFragment()
        }
    }
}
