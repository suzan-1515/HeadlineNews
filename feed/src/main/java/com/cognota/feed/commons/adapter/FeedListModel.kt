package com.cognota.feed.commons.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.cognota.feed.commons.domain.FeedDTO
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


@EpoxyModelClass(layout = R2.layout.item_list_feed)
abstract class FeedListModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<FeedListModel.Holder>() {

    @EpoxyAttribute
    lateinit var feed: FeedDTO
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

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
            feed.thumbnail()?.let {
                picasso.load(it)
                    .fit()
                    .transform(RoundedCornersTransformation(16, 4))
                    .into(holder.image)
            } ?: run {
                holder.image.visibility = View.GONE
            }
            holder.category.text = feed.category.name
            feed.source.favIcon().let {
                picasso.load(it)
                    .resize(64, 64)
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.sourceIcon)
            }

            if (::clickListener.isInitialized) {
                holder.card.setOnClickListener(clickListener)
                holder.option.setOnClickListener(clickListener)
            }

            ViewCompat.setTransitionName(holder.title, "title$feed.id")
            ViewCompat.setTransitionName(holder.preview, "preview$feed.id")
            ViewCompat.setTransitionName(holder.date, "date$feed.id")
            ViewCompat.setTransitionName(holder.image, "image$feed.id")
            ViewCompat.setTransitionName(holder.sourceIcon, "source_icon$feed.id")
            ViewCompat.setTransitionName(holder.category, "category$feed.id")
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
        holder.option.setOnClickListener(null)
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
        val category by bind<AppCompatTextView>(R.id.category)
        val option by bind<ImageView>(R.id.option)
    }

}
