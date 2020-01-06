package com.cognota.feed.list.adapter

import android.content.Context
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.cognota.feed.commons.domain.FeedWithRelatedFeedDTO
import com.squareup.picasso.Picasso
import com.yarolegovich.discretescrollview.DiscreteScrollView


@EpoxyModelClass(layout = R2.layout.item_card_feed_layout)
abstract class FeedStackModel(
    private val context: Context,
    private val picasso: Picasso
) :
    EpoxyModelWithHolder<FeedStackModel.Holder>() {

    @EpoxyAttribute
    lateinit var feed: FeedWithRelatedFeedDTO

    override fun bind(holder: Holder) {
        if (::feed.isInitialized) {
            val controller = FeedCardStackController(context, picasso)
            holder.scrollview.adapter = controller.adapter
            controller.setData(feed)
        }
    }

    override fun unbind(holder: Holder) {
    }

    inner class Holder : BaseEpoxyHolder() {
        val scrollview by bind<DiscreteScrollView>(R.id.scrollview)
    }

}
