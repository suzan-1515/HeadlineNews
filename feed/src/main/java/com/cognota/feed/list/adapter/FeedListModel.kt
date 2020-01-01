package com.cognota.feed.list.adapter

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso


@EpoxyModelClass(layout = R2.layout.item_feed)
abstract class FeedListModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<FeedListModel.Holder>() {

    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var image: String
    @EpoxyAttribute
    lateinit var preview: String
    @EpoxyAttribute
    lateinit var date: String
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: Holder) {
        title.let { holder.title.text = it }
        preview.let { holder.preview.text = it }
        date.let { holder.date.text = it }
        image.let {
            picasso.load(it).into(holder.image)
        }
        clickListener.let {
            holder.card.setOnClickListener(it)
        }
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
        holder.preview.text = null
        holder.date.text = null
        picasso.cancelRequest(holder.image)
    }

    inner class Holder : BaseEpoxyHolder() {
        val card by bind<MaterialCardView>(R.id.card)
        val image by bind<AppCompatImageView>(R.id.image)
        val title by bind<TextView>(R.id.title)
        val preview by bind<TextView>(R.id.preview)
        val date by bind<TextView>(R.id.date)
    }

}
