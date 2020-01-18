package com.cognota.feed.personalised.adapter

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.cognota.feed.commons.adapter.BaseEpoxyHolder
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.personalised.ui.PersonalisedFeedFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@EpoxyModelClass(layout = R2.layout.item_mini_card_feed)
abstract class FeedMiniCardModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<FeedMiniCardModel.Holder>() {

    @EpoxyAttribute
    lateinit var feed: FeedDTO

    override fun bind(holder: Holder) {
        if (::feed.isInitialized) {
            holder.title.text = feed.title
            holder.date.text =
                holder.date.context.getString(
                    R.string.source_with_time,
                    feed.source.name,
                    feed.publishedDate()
                )
            feed.image.let {
                picasso.load(it)
                    .into(holder.image)
            }
            feed.source.icon().let {
                picasso.load(it)
                    .resize(64, 64)
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.sourceIcon)
            }
            holder.card.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    holder.title to holder.title.transitionName,
                    holder.date to holder.date.transitionName,
                    holder.image to holder.image.transitionName,
                    holder.sourceIcon to holder.sourceIcon.transitionName
                )
                it.findNavController().navigate(
                    PersonalisedFeedFragmentDirections.detailAction(
                        feed = feed
                    ),
                    extras
                )
            }

            ViewCompat.setTransitionName(holder.title, "title$feed.id")
            ViewCompat.setTransitionName(holder.date, "date$feed.id")
            ViewCompat.setTransitionName(holder.image, "image$feed.id")
            ViewCompat.setTransitionName(holder.sourceIcon, "source_icon$feed.id")
        }
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
        holder.date.text = null
        holder.card.setOnClickListener(null)
        ViewCompat.setTransitionName(holder.title, null)
        ViewCompat.setTransitionName(holder.date, null)
        ViewCompat.setTransitionName(holder.image, null)
        ViewCompat.setTransitionName(holder.sourceIcon, null)
    }

    inner class Holder : BaseEpoxyHolder() {
        val card by bind<MaterialCardView>(R.id.card)
        val image by bind<AppCompatImageView>(R.id.image)
        val sourceIcon by bind<CircleImageView>(R.id.sourceIcon)
        val title by bind<TextView>(R.id.title)
        val date by bind<TextView>(R.id.date)
    }

}
