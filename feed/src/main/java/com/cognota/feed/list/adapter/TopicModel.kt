package com.cognota.feed.list.adapter

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@EpoxyModelClass(layout = R2.layout.item_topic)
abstract class TopicModel(private val picasso: Picasso) :
    EpoxyModelWithHolder<TopicModel.Holder>() {

    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var icon: String
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: Holder) {
        clickListener.let {
            holder.card.setOnClickListener(
                it
            )
        }
        title.let { holder.title.text = it }
        icon.let {
            picasso.load(it).into(holder.icon)
        }
    }

    override fun unbind(holder: Holder) {
        holder.card.setOnClickListener(null)
        picasso.cancelRequest(holder.icon)
        holder.title.text = null
    }

    inner class Holder : BaseEpoxyHolder() {
        val card by bind<MaterialCardView>(R.id.card)
        val icon by bind<CircleImageView>(R.id.image)
        val title by bind<TextView>(R.id.title)
    }

}
