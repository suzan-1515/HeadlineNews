package com.cognota.feed.bookmark.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2
import com.cognota.feed.commons.adapter.BaseEpoxyHolder


@EpoxyModelClass(layout = R2.layout.item_reset_saved_feed)
abstract class ClearBookmarkModel :
    EpoxyModelWithHolder<ClearBookmarkModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: Holder) {
        if (::clickListener.isInitialized) {
            holder.clearAll.setOnClickListener(clickListener)
        }
    }

    override fun unbind(holder: Holder) {
        holder.clearAll.setOnClickListener(null)
    }

    inner class Holder : BaseEpoxyHolder() {
        val clearAll by bind<AppCompatImageView>(R.id.clearAll)
    }

}
