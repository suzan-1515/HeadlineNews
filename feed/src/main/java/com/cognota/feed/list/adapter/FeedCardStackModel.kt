package com.cognota.feed.list.adapter

import android.content.Context
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


@EpoxyModelClass(layout = R2.layout.item_card_stack_feed)
abstract class FeedCardStackModel(private val picasso: Picasso, private val context: Context) :
    EpoxyModelWithHolder<FeedCardStackModel.Holder>() {

    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var image: Uri
    @EpoxyAttribute
    lateinit var sourceIcon: Uri
    @EpoxyAttribute
    lateinit var preview: String
    @EpoxyAttribute
    lateinit var date: String
    @EpoxyAttribute
    lateinit var source: String
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: Holder) {
        title.let { holder.title.text = it }
        preview.let { holder.preview.text = it }
        if (::date.isInitialized && ::source.isInitialized)
            date.let {
                holder.date.text = context.getString(R.string.source_with_time, source, date)
            }
        if (::image.isInitialized) {
            picasso.load(image)
                .into(holder.image)
        } else {
//            holder.image.visibility = View.GONE
        }
        if (::sourceIcon.isInitialized) {
            picasso.load(sourceIcon)
                .resize(64, 64)
                .onlyScaleDown()
                .centerInside()
                .into(holder.sourceIcon)
        } else {
            picasso.load(R.drawable.ic_block_black_24dp)
                .into(holder.sourceIcon)
        }
        clickListener.let {
            holder.card.setOnClickListener(it)
        }

        ViewCompat.setTransitionName(holder.title, "title" + id())
        ViewCompat.setTransitionName(holder.preview, "preview" + id())
        ViewCompat.setTransitionName(holder.date, "date" + id())
        ViewCompat.setTransitionName(holder.image, "image" + id())
        ViewCompat.setTransitionName(holder.sourceIcon, "source_icon" + id())
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
        holder.preview.text = null
        holder.date.text = null
        picasso.cancelRequest(holder.image)
        picasso.cancelRequest(holder.sourceIcon)
        ViewCompat.setTransitionName(holder.title, null)
        ViewCompat.setTransitionName(holder.preview, null)
        ViewCompat.setTransitionName(holder.date, null)
        ViewCompat.setTransitionName(holder.image, null)
        ViewCompat.setTransitionName(holder.sourceIcon, null)
    }

    inner class Holder : BaseEpoxyHolder() {
        val card by bind<MaterialCardView>(R.id.card)
        val image by bind<AppCompatImageView>(R.id.image)
        val sourceIcon by bind<CircleImageView>(R.id.sourceIcon)
        val title by bind<TextView>(R.id.title)
        val preview by bind<TextView>(R.id.preview)
        val date by bind<TextView>(R.id.date)
    }

}
