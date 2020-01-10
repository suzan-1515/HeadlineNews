package com.cognota.feed.list.adapter

import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@EpoxyModelClass(layout = R2.layout.item_mini_card_feed)
abstract class TrendingFeedMiniCardModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<TrendingFeedMiniCardModel.Holder>() {

    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var image: Uri
    @EpoxyAttribute
    lateinit var sourceIcon: Uri
    @EpoxyAttribute
    lateinit var date: String
    @EpoxyAttribute
    lateinit var source: String
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

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
        clickListener.let {
            holder.card.setOnClickListener(it)
        }

        ViewCompat.setTransitionName(holder.title, "title" + id())
        ViewCompat.setTransitionName(holder.date, "date" + id())
        ViewCompat.setTransitionName(holder.image, "image" + id())
        ViewCompat.setTransitionName(holder.sourceIcon, "sourceIcon" + id())
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
        holder.date.text = null
        picasso.cancelRequest(holder.image)
        picasso.cancelRequest(holder.sourceIcon)
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
