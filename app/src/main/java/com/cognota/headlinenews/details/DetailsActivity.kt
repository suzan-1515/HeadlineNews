package com.cognota.headlinenews.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.application.CoreApp
import com.cognota.core.ui.BaseActivity
import com.cognota.headlinenews.R
import com.cognota.headlinenews.commons.di.NewsSharedDependencyProvider
import com.cognota.headlinenews.commons.domain.FeedDTO
import com.cognota.headlinenews.details.viewmodel.DetailsViewModel
import com.cognota.headlinenews.details.viewmodel.DetailsViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import timber.log.Timber
import javax.inject.Inject

class DetailsActivity : BaseActivity() {

    companion object {
        private const val SELECTED_FEED = "feed"
        //Transitions
        private const val TITLE_TRANSITION_NAME = "title_transition"
        private const val BODY_TRANSITION_NAME = "body_transition"
        private const val AUTHOR_TRANSITION_NAME = "author_transition"
        private const val AVATAR_TRANSITION_NAME = "avatar_transition"

        fun start(
            context: Context,
            feed: FeedDTO,
            tvTitle: TextView,
            tvBody: TextView,
            tvAuthorName: TextView,
            ivAvatar: ImageView
        ) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(SELECTED_FEED, feed)

            //Transitions
            intent.putExtra(TITLE_TRANSITION_NAME, ViewCompat.getTransitionName(tvTitle))
            intent.putExtra(BODY_TRANSITION_NAME, ViewCompat.getTransitionName(tvBody))
            intent.putExtra(AUTHOR_TRANSITION_NAME, ViewCompat.getTransitionName(tvAuthorName))
            intent.putExtra(AVATAR_TRANSITION_NAME, ViewCompat.getTransitionName(ivAvatar))

            val p1 = Pair.create(tvTitle as View, ViewCompat.getTransitionName(tvTitle))
            val p2 = Pair.create(tvBody as View, ViewCompat.getTransitionName(tvBody))
            val p3 = Pair.create(tvAuthorName as View, ViewCompat.getTransitionName(tvAuthorName))
            val p4 = Pair.create(ivAvatar as View, ViewCompat.getTransitionName(ivAvatar))
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                p1,
                p2,
                p3,
                p4
            )

            context.startActivity(intent, options.toBundle())
        }

        fun start(context: Context, postId: Int) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(SELECTED_FEED, postId)
            context.startActivity(intent)
        }
    }

    private var selectedFeed: FeedDTO? = null
    private val context: Context by lazy { this }

    @Inject
    lateinit var viewModelFactory: DetailsViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as CoreApp).coreComponent
        NewsSharedDependencyProvider.detailsComponent(appComponent).inject(this)
        setContentView(R.layout.activity_details)

        getIntentData()
    }

    private fun getIntentData() {
        if (!intent.hasExtra(SELECTED_FEED)) {
            Timber.d("getIntentData: could not find selected post")
            finish()
            return
        }

        selectedFeed = intent.getSerializableExtra(SELECTED_FEED) as FeedDTO?
        tvTitle.text = selectedFeed?.title
        tvBody.text = selectedFeed?.description
        tvAuthorName.text = selectedFeed?.source
        picasso.load(selectedFeed?.image).into(ivAvatar)

        handleTransition(intent.extras)

        observeData()
    }

    private fun handleTransition(extras: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvTitle.transitionName = extras?.getString(TITLE_TRANSITION_NAME)
            tvBody.transitionName = extras?.getString(BODY_TRANSITION_NAME)
            tvAuthorName.transitionName = extras?.getString(AUTHOR_TRANSITION_NAME)
            ivAvatar.transitionName = extras?.getString(AVATAR_TRANSITION_NAME)
        }
    }

    private fun observeData() {

    }

}
