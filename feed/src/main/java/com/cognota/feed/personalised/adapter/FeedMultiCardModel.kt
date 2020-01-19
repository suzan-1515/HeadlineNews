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
import com.cognota.feed.commons.domain.RelatedFeedDTO
import com.cognota.feed.personalised.ui.PersonalisedFeedFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@EpoxyModelClass(layout = R2.layout.item_multi_card_feed)
abstract class FeedMultiCardModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<FeedMultiCardModel.Holder>() {

    @EpoxyAttribute
    lateinit var feed: FeedDTO
    @EpoxyAttribute
    lateinit var relatedFeed: RelatedFeedDTO

    override fun bind(holder: Holder) {
        if (::feed.isInitialized) {
            holder.title.text = feed.title
            holder.preview.text = feed.description
            holder.date.text =
                holder.date.context.getString(
                    R.string.source_with_time,
                    feed.source.name,
                    feed.publishedDate()
                )
            feed.image?.let {
                picasso.load(it)
                    .into(holder.image)
            }
            holder.category.text = feed.category.name
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
                    holder.preview to holder.preview.transitionName,
                    holder.date to holder.date.transitionName,
                    holder.image to holder.image.transitionName,
                    holder.sourceIcon to holder.sourceIcon.transitionName,
                    holder.category to holder.category.transitionName
                )
                it.findNavController().navigate(
                    PersonalisedFeedFragmentDirections.detailAction(
                        feed = feed
                    ),
                    extras
                )
            }

            ViewCompat.setTransitionName(holder.title, "title$feed.id")
            ViewCompat.setTransitionName(holder.preview, "preview$feed.id")
            ViewCompat.setTransitionName(holder.date, "date$feed.id")
            ViewCompat.setTransitionName(holder.image, "image$feed.id")
            ViewCompat.setTransitionName(holder.sourceIcon, "source_icon$feed.id")
            ViewCompat.setTransitionName(holder.category, "category$feed.id")
        } else if (::relatedFeed.isInitialized) {
            holder.title.text = relatedFeed.title
            holder.preview.text = relatedFeed.description
            holder.date.text =
                holder.date.context.getString(
                    R.string.source_with_time,
                    relatedFeed.source.name,
                    relatedFeed.publishedDate()
                )
            relatedFeed.image?.let {
                picasso.load(it)
                    .into(holder.image)
            }
            holder.category.text = relatedFeed.category.name
            relatedFeed.source.icon().let {
                picasso.load(it)
                    .resize(64, 64)
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.sourceIcon)
            }
            holder.card.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    holder.title to holder.title.transitionName,
                    holder.preview to holder.preview.transitionName,
                    holder.date to holder.date.transitionName,
                    holder.image to holder.image.transitionName,
                    holder.sourceIcon to holder.sourceIcon.transitionName,
                    holder.category to holder.category.transitionName
                )
                it.findNavController().navigate(
                    PersonalisedFeedFragmentDirections.detailAction(
                        feed = feed
                    ),
                    extras
                )
            }

            ViewCompat.setTransitionName(holder.title, "title$relatedFeed.id")
            ViewCompat.setTransitionName(holder.preview, "preview$relatedFeed.id")
            ViewCompat.setTransitionName(holder.date, "date$relatedFeed.id")
            ViewCompat.setTransitionName(holder.image, "image$relatedFeed.id")
            ViewCompat.setTransitionName(holder.sourceIcon, "source_icon$relatedFeed.id")
            ViewCompat.setTransitionName(holder.category, "category$relatedFeed.id")
        }
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
        holder.preview.text = null
        holder.date.text = null
        holder.category.text = null
        picasso.cancelRequest(holder.image)
        picasso.cancelRequest(holder.sourceIcon)
        holder.card.setOnClickListener(null)
        ViewCompat.setTransitionName(holder.title, null)
        ViewCompat.setTransitionName(holder.preview, null)
        ViewCompat.setTransitionName(holder.date, null)
        ViewCompat.setTransitionName(holder.image, null)
        ViewCompat.setTransitionName(holder.sourceIcon, null)
        ViewCompat.setTransitionName(holder.category, null)
    }


    inner class Holder : BaseEpoxyHolder() {
        val card by bind<MaterialCardView>(R.id.card)
        val image by bind<AppCompatImageView>(R.id.image)
        val sourceIcon by bind<CircleImageView>(R.id.sourceIcon)
        val title by bind<TextView>(R.id.title)
        val preview by bind<TextView>(R.id.preview)
        val date by bind<TextView>(R.id.date)
        val category by bind<Chip>(R.id.category)
    }

}
