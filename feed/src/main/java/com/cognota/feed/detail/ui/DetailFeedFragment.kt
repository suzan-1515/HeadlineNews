package com.cognota.feed.detail.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
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
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.detail.adapter.RelatedFeedController
import com.cognota.feed.detail.viewmodel.DetailFeedViewModel
import com.cognota.feed.detail.viewmodel.DetailFeedViewModelFactory
import com.cognota.feed.option.data.OptionEvent
import com.cognota.feed.option.viewmodel.FeedOptionViewModel
import com.cognota.feed.option.viewmodel.FeedOptionViewModelFactory
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

    private val args: DetailFeedFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: DetailFeedViewModelFactory

    @Inject
    lateinit var feedController: RelatedFeedController

    private val viewModel: DetailFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailFeedViewModel::class.java)
    }

    @Inject
    lateinit var feedOptionViewModelFactory: FeedOptionViewModelFactory

    private val feedOptionViewModel: FeedOptionViewModel by lazy {
        ViewModelProviders.of(requireActivity(), feedOptionViewModelFactory)
            .get(FeedOptionViewModel::class.java)
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
            ViewCompat.setTransitionName(date, "date${feed.publishedDateRelative()}")
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
        toolbar.inflateMenu(R.menu.feed_detail)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.option -> {
                    args.feed?.let {
                        view.findNavController().navigate(
                            DetailFeedFragmentDirections.menuAction(
                                feed = it
                            )
                        )
                    }
                    true
                }
                else -> {
                    super.onOptionsItemSelected(menuItem)
                }
            }
        }
        rv.setControllerAndBuildModels(feedController)
        shareIcon.setOnClickListener {
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
            viewModel.getRelatedFeed(it.parentId?.let { it } ?: it.id)
            feedOptionViewModel.getBookmarkedStatus(it.id)
            feedOptionViewModel.getLikeStatus(it.id)
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
                    feed.publishedDate()
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
            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        feedController.setLoading(resource.isLoading())
                    }
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            feedController.setFeeds(resource.getData())
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
                            feedController.setLoading(resource.isLoading())
                        }
                        snackBar?.show()
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        Timber.d("Network error")
                        snackBar = Snackbar.make(
                            root,
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
                            root,
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

        feedOptionViewModel.optionEventLiveData.observe(
            viewLifecycleOwner,
            Observer { data ->
                when (data) {
                    OptionEvent.BOOKMARKED -> {
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                R.string.feed_bookmarked
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    OptionEvent.UNBOOKMARKED -> {
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                R.string.feed_bookmarked_removed
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    OptionEvent.HIDDEN -> {
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                R.string.news_hidden_message
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    OptionEvent.LIKED -> {
                        Timber.d("News liked")
                    }
                    OptionEvent.DISLIKED -> {
                        Timber.d("News disliked")
                    }
                    else -> {
                        Timber.d("Unknown bookmark state")
                    }
                }
            }
        )
        feedOptionViewModel.bookmarkStatusLiveData.observe(
            viewLifecycleOwner,
            Observer { data ->
                when (data) {
                    OptionEvent.BOOKMARKED -> {
                        bookmarkIcon.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.md_orange_500
                            )
                        )
                    }
                    OptionEvent.UNBOOKMARKED -> {
                        bookmarkIcon.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.md_grey_600
                            )
                        )
                    }
                    else -> {
                        Timber.d("Unknown bookmark status")
                    }
                }
                setBookmarkActionListener(data)
            }
        )

        feedOptionViewModel.likeStatusLiveData.observe(
            viewLifecycleOwner,
            Observer { data ->
                when (data) {
                    OptionEvent.LIKED -> {
                        likeIcon.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.md_orange_500
                            )
                        )
                    }
                    OptionEvent.DISLIKED -> {
                        bookmarkIcon.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.md_grey_600
                            )
                        )
                    }
                    else -> {
                        Timber.d("Unknown bookmark status")
                    }
                }
                setLikeActionListener(data)
            }
        )

    }

    private fun setBookmarkActionListener(event: OptionEvent) {
        args.feed?.let { feed ->
            when (event) {
                OptionEvent.BOOKMARKED -> {
                    bookmarkIcon.setOnClickListener {
                        feedOptionViewModel.unBookmarkFeed(feed)
                    }
                }
                OptionEvent.UNBOOKMARKED -> {
                    bookmarkIcon.setOnClickListener {
                        feedOptionViewModel.bookmarkFeed(feed)
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun setLikeActionListener(event: OptionEvent) {
        args.feed?.let { feed ->
            when (event) {
                OptionEvent.LIKED -> {
                    likeIcon.setOnClickListener {
                        feedOptionViewModel.dislikeFeed(feed)
                    }
                }
                OptionEvent.DISLIKED -> {
                    likeIcon.setOnClickListener {
                        feedOptionViewModel.likeFeed(feed)
                    }
                }
                else -> {
                }
            }
        }
    }

    companion object {
        fun newInstance(): DetailFeedFragment {
            return DetailFeedFragment()
        }
    }
}
