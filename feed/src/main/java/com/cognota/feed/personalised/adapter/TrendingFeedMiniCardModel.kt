package com.cognota.feed.personalised.adapter

import android.net.Uri
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
import com.cognota.feed.commons.domain.FeedType
import com.cognota.feed.personalised.ui.PersonalisedFeedFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@EpoxyModelClass(layout = R2.layout.item_mini_card_feed)
abstract class TrendingFeedMiniCardModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<TrendingFeedMiniCardModel.Holder>() {

    @EpoxyAttribute
    lateinit var uuid: String
    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    var preview: String? = null
    @EpoxyAttribute
    lateinit var image: Uri
    @EpoxyAttribute
    lateinit var sourceIcon: Uri
    @EpoxyAttribute
    lateinit var date: String
    @EpoxyAttribute
    lateinit var source: String
    @EpoxyAttribute
    lateinit var category: String
    @EpoxyAttribute
    lateinit var type: FeedType

    override fun bind(holder: Holder) {
        title.let { holder.title.text = it }
        if (::date.isInitialized && ::source.isInitialized)
            date.let {
                holder.date.text =
                    holder.date.context.getString(R.string.source_with_time, source, date)
            }
        if (::image.isInitialized) {
            picasso.load(image)
                .into(holder.image)
        }
        if (::sourceIcon.isInitialized) {
            picasso.load(sourceIcon)
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
                    id = uuid,
                    title = title,
                    image = image.toString(),
                    description = preview,
                    publishedDate = date,
                    sourceTitle = source,
                    sourceIcon = sourceIcon.toString(),
                    categoryTitle = category,
                    type = type
                ),
                extras
            )
        }

        ViewCompat.setTransitionName(holder.title, "title$uuid")
        ViewCompat.setTransitionName(holder.date, "date$uuid")
        ViewCompat.setTransitionName(holder.image, "image$uuid")
        ViewCompat.setTransitionName(holder.sourceIcon, "source_icon$uuid")
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
        holder.date.text = null
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
