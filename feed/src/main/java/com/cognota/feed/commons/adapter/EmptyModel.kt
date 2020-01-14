package com.cognota.feed.commons.adapter

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2


@EpoxyModelClass(layout = R2.layout.item_empty)
abstract class EmptyModel :
    EpoxyModelWithHolder<EmptyModel.Holder>() {

    @EpoxyAttribute
    lateinit var title: String

    override fun bind(holder: Holder) {
        holder.title.text = title
    }

    override fun unbind(holder: Holder) {
        holder.title.text = null
    }

    inner class Holder : BaseEpoxyHolder() {
        val title by bind<TextView>(R.id.title)
    }

}
