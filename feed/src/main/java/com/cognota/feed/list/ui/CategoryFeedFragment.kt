package com.cognota.feed.list.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.ui.BaseFragment
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.list.adapter.CategoryFeedController
import com.cognota.feed.list.viewmodel.CategoryFeedViewModel
import com.cognota.feed.list.viewmodel.CategoryFeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_category_feed.*
import timber.log.Timber
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class CategoryFeedFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: CategoryFeedViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private val viewModel: CategoryFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CategoryFeedViewModel::class.java)
    }

    private lateinit var feedController: CategoryFeedController

    private var snackBar: Snackbar? = null

    private var categoryCodeParam: String? = null

    private var page = 1

    override fun onAttach(context: Context) {
        (activity as FeedActivity).categoryFeedComponent
            ?.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initiateDataListener()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryCodeParam = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("category fragment")

        feedController = CategoryFeedController(picasso)
        rv.setControllerAndBuildModels(feedController)
        srl.setOnRefreshListener { viewModel.getFeeds(page) }
        categoryCodeParam?.let { code ->
            viewModel.categoryCode(code)
        }
    }

    private fun initiateDataListener() {
        viewModel.feeds.observe(
            viewLifecycleOwner,
            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        feedController.setLoading(resource.isLoading())
                    }
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            Timber.d("Feed data received ")
                            feedController.setFeeds(resource.getData())
                            page++
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                srl,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
                                ),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.ok) { snackBar?.dismiss() }
                        }
                        snackBar?.show()
                        feedController.setLoading(resource.isLoading())
                        srl.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        Timber.d("Network error")
                        snackBar = Snackbar.make(
                            srl,
                            getString(
                                resource.message
                                    ?: R.string.no_network_connection
                            ),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getFeeds(page)
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        feedController.setLoading(resource.isLoading())
                        srl.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.ERROR_API -> {
                        Timber.d("Api error")
                        snackBar = Snackbar.make(
                            srl,
                            getString(
                                resource.message ?: R.string.service_error
                            ), Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getFeeds(page)
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        feedController.setLoading(resource.isLoading())
                        srl.isRefreshing = resource.isLoading()
                    }
                    else -> Timber.d("Unknown state")
                }
            })

        viewModel.categoryCode.observe(viewLifecycleOwner, Observer<String> {
            viewModel.getFeeds(page)
        })

    }

    companion object {
        @JvmStatic
        fun newInstance(categoryCode: String?): CategoryFeedFragment {
            return CategoryFeedFragment().apply {
                arguments = bundleOf(ARG_PARAM1 to categoryCode)
            }
        }
    }
}
