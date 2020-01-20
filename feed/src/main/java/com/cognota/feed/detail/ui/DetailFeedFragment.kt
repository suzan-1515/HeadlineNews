package com.cognota.feed.detail.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.cognota.core.di.FeatureScope
import com.cognota.core.ui.BaseFragment
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.commons.domain.RelatedFeedDTO
import com.cognota.feed.commons.viewmodel.BookmarkFeedViewModel
import com.cognota.feed.commons.viewmodel.BookmarkFeedViewModelFactory
import com.cognota.feed.detail.adapter.RelatedFeedController
import com.cognota.feed.detail.viewmodel.DetailFeedViewModel
import com.cognota.feed.detail.viewmodel.DetailFeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_detail.*
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@FeatureScope
class DetailFeedFragment : BaseFragment() {

    val args: DetailFeedFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: DetailFeedViewModelFactory

    @Inject
    lateinit var feedController: RelatedFeedController

    private val viewModel: DetailFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailFeedViewModel::class.java)
    }

    @Inject
    lateinit var bookmarkViewModelFactory: BookmarkFeedViewModelFactory

    private val bookmarkViewModel: BookmarkFeedViewModel by lazy {
        ViewModelProviders.of(requireActivity(), bookmarkViewModelFactory)
            .get(BookmarkFeedViewModel::class.java)
    }

    @Inject
    lateinit var picasso: Picasso

    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        Timber.d("attaching detail feed fragment")
        (activity as DetailFeedActivity).feedComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initiateDataListener()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_detail, container, false)
    }

    private fun prepareSharedTransition() {
        args.feed?.let { feed ->
            ViewCompat.setTransitionName(title, "title$feed.id")
            ViewCompat.setTransitionName(description, "preview$feed.id")
            ViewCompat.setTransitionName(image, "image$feed.id")
            ViewCompat.setTransitionName(sourceIcon, "source_icon$feed.id")
            ViewCompat.setTransitionName(date, "date${feed.publishedDate()}")
            ViewCompat.setTransitionName(category, "category$feed.id")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("detail fragment: onViewCreated")

        prepareSharedTransition()
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.inflateMenu(R.menu.menu_feed_detail)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_save -> {
                    args.feed?.let {
                        bookmarkViewModel.bookmarkFeed(it)
                        snackBar = Snackbar.make(
                            view,
                            getString(
                                R.string.feed_bookmarked
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    true
                }
                else -> {
                    super.onOptionsItemSelected(menuItem)
                }
            }
        }
        rv.setControllerAndBuildModels(feedController)
        share.setOnClickListener {
            args.feed?.let {
                it.link()?.let { link ->
                    ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setChooserTitle("Share this news")
                        .setSubject(it.title)
                        .setText(
                            requireContext().getString(
                                R.string.news_share_format,
                                it.title,
                                link.toString()
                            )
                        )
                        .startChooser()
                }
            }
        }
        feedData()
        args.feed?.let {
            viewModel.getRelatedFeed(it.id)
        }
    }

    private fun feedData() {
        args.feed?.let { feed ->
            feed.image?.let {
                picasso.load(it).into(image)
            }
            title.text = feed.title
            description.text = feed.description
            feed.source.icon()?.let {
                picasso.load(it)
                    .resize(64, 64)
                    .onlyScaleDown()
                    .centerInside()
                    .into(sourceIcon)
            }
            date.text =
                context?.getString(
                    R.string.source_with_time,
                    feed.source.name,
                    feed.publishedDateRaw()
                )
            category.text = feed.category.name

            feed.link()?.let { link ->
                readMore.setOnClickListener {
                    it?.findNavController()?.navigate(
                        DetailFeedFragmentDirections.webviewAction(
                            feed = args.feed
                        )
                    )
                }
            }
        }
    }

    private fun initiateDataListener() {
//        Observe the outcome and update state of the screen accordingly
        viewModel.relatedFeeds.observe(
            viewLifecycleOwner,
            Observer<StatefulResource<List<RelatedFeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        feedController.setLoading(resource.isLoading())
                    }
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            Timber.d("Feed data received ")
                            feedController.setFeeds(resource.getData())
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                coordinator_layout.rootView,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
                                ),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.ok) { snackBar?.dismiss() }
                            feedController.setLoading(resource.isLoading())
                        }
                        snackBar?.show()
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        Timber.d("Network error")
                        snackBar = Snackbar.make(
                            coordinator_layout.rootView,
                            getString(
                                resource.message
                                    ?: R.string.no_network_connection
                            ),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                args.feed?.let {
                                    viewModel.getRelatedFeed(it.id)
                                }
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        feedController.setLoading(false)
                    }
                    StatefulResource.State.ERROR_API -> {
                        Timber.d("Api error")
                        snackBar = Snackbar.make(
                            coordinator_layout.rootView,
                            getString(
                                resource.message ?: R.string.service_error
                            ), Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                args.feed?.let {
                                    viewModel.getRelatedFeed(it.id)
                                }
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        feedController.setLoading(false)
                    }
                    else -> Timber.d("Unknown state")
                }
            })

    }

    companion object {
        fun newInstance(): DetailFeedFragment {
            return DetailFeedFragment()
        }
    }
}
