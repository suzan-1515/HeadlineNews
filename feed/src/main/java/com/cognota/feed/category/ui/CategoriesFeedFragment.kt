package com.cognota.feed.category.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.cognota.core.di.FeatureScope
import com.cognota.core.ui.BaseFragment
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.cognota.feed.category.viewmodel.CategoriesFeedViewModel
import com.cognota.feed.category.viewmodel.CategoriesFeedViewModelFactory
import com.cognota.feed.commons.domain.SourceAndCategoryDTO
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_categories_feed.*
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class CategoriesFeedFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: CategoriesFeedViewModelFactory

    private lateinit var fragmentAdapterFeed: CategoryFeedPagerAdapter

    private val viewModel: CategoriesFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CategoriesFeedViewModel::class.java)
    }

    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        val feedActivity = activity as FeedActivity
        val categoryFeedComponent = feedActivity.feedComponent
            .categoryFeedComponent()
            .create()
        categoryFeedComponent.inject(this)
        feedActivity.categoryFeedComponent = categoryFeedComponent
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
        return inflater.inflate(R.layout.fragment_categories_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("feed categories fragment")

        fragmentAdapterFeed = CategoryFeedPagerAdapter(this)
        viewpager.offscreenPageLimit = 1
        viewpager.adapter = fragmentAdapterFeed
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = fragmentAdapterFeed.getPageTitle(position)
        }.attach()
        srl.isEnabled = false

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.currentCategory(position)
            }
        })
    }

    private fun initiateDataListener() {
        viewModel.categories.observe(
            viewLifecycleOwner,
            Observer<StatefulResource<SourceAndCategoryDTO?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        srl.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.SUCCESS -> {
                        srl.isRefreshing = resource.isLoading()
                        if (resource.hasData()) {
                            fragmentAdapterFeed.setData(resource.getData())
//                            viewModel.selectedCategory.value?.let { viewpager.currentItem = it }
                        } else {
                            snackBar = Snackbar.make(
                                root,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
                                ),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.ok) { snackBar?.dismiss() }
                            snackBar?.show()
                        }
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                resource.message
                                    ?: R.string.no_network_connection
                            ),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getCategories()
                                snackBar?.dismiss()
                            }
                        srl.isRefreshing = resource.isLoading()
                        snackBar?.show()
                    }
                    StatefulResource.State.ERROR_API -> {
                        Timber.d("Api error")
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                resource.message ?: R.string.service_error
                            ), Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getCategories()
                                snackBar?.dismiss()
                            }
                        srl.isRefreshing = resource.isLoading()
                        snackBar?.show()
                    }
                    else -> Timber.d("Unknown state")
                }
            })
    }

    override fun onDestroy() {
        (activity as FeedActivity).categoryFeedComponent = null
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoriesFeedFragment()
    }
}
