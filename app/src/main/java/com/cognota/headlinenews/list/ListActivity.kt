package com.cognota.headlinenews.list

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.application.CoreApp
import com.cognota.core.ui.BaseActivity
import com.cognota.core.ui.StatefulResource
import com.cognota.headlinenews.R
import com.cognota.headlinenews.commons.di.NewsSharedDependencyProvider
import com.cognota.headlinenews.commons.domain.FeedDTO
import com.cognota.headlinenews.details.DetailsActivity
import com.cognota.headlinenews.list.viewmodel.ListViewModel
import com.cognota.headlinenews.list.viewmodel.ListViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_list.*
import timber.log.Timber
import javax.inject.Inject

class ListActivity : BaseActivity(), FeedListAdapter.Interaction {

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    @Inject
    lateinit var adapter: FeedListAdapter

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val context: Context by lazy { this }

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (applicationContext as CoreApp).coreComponent
        NewsSharedDependencyProvider.listComponent(appComponent).inject(this)

        setContentView(R.layout.activity_list)

        adapter.interaction = this
        rvPosts.adapter = adapter
        srlPosts.setOnRefreshListener { viewModel.getLatestFeed() }

        initiateDataListener()
        viewModel.getLatestFeed()
    }

    private fun initiateDataListener() {
        //Observe the outcome and update state of the screen  accordingly
        viewModel.latestFeeds.observe(this, Observer<StatefulResource<List<FeedDTO>?>> { resource ->
            snackBar?.dismiss()

            when (resource.state) {
                StatefulResource.State.LOADING -> {
                    srlPosts.isRefreshing = true
                    //                    snackBar =
                    //                        Snackbar.make(
                    //                            view_pager_wrapper,
                    //                            getString(R.string.searching),
                    //                            Snackbar.LENGTH_INDEFINITE
                    //                        )
                    //                    snackBar?.show()
                }
                StatefulResource.State.SUCCESS -> {
                    if (resource.hasData()) {
                        Timber.d("Data received %d", resource.getData()?.size)
                        adapter.swapData(resource.getData()!!)
                    } else {
                        Timber.d("Empty data received")
                        snackBar = Snackbar.make(
                            srlPosts.rootView,
                            getString(resource.message ?: R.string.news_feed_not_found),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.ok) { snackBar?.dismiss() }
                    }
                    snackBar?.show()
                    srlPosts.isRefreshing = false
                }
                StatefulResource.State.ERROR_NETWORK -> {
                    Timber.d("Network error")
                    snackBar = Snackbar.make(
                        srlPosts.rootView,
                        getString(resource.message ?: R.string.no_network_connection),
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.retry) {
                            viewModel.getLatestFeed()
                            snackBar?.dismiss()
                        }
                    snackBar?.show()
                    srlPosts.isRefreshing = false
                }
                StatefulResource.State.ERROR_API -> {
                    Timber.d("Api error")
                    snackBar = Snackbar.make(
                        srlPosts.rootView,
                        getString(resource.message ?: R.string.service_error), Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.retry) {
                            viewModel.getLatestFeed()
                            snackBar?.dismiss()
                        }
                    snackBar?.show()
                    srlPosts.isRefreshing = false
                }
                else -> Timber.d("Unknown state")
            }
        })
    }

    override fun feedClicked(
        feed: FeedDTO,
        tvTitle: TextView,
        tvBody: TextView,
        tvAuthorName: TextView,
        ivAvatar: ImageView
    ) {
        DetailsActivity.start(context, feed, tvTitle, tvBody, tvAuthorName, ivAvatar)
    }

}
