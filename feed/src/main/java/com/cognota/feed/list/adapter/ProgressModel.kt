package com.cognota.feed.list.adapter

import androidx.core.widget.ContentLoadingProgressBar
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cognota.feed.R
import com.cognota.feed.R2


@EpoxyModelClass(layout = R2.layout.item_loading)
abstract class ProgressModel :
    EpoxyModelWithHolder<ProgressModel.Holder>() {

    override fun bind(holder: Holder) {
    }

    override fun unbind(holder: Holder) {
    }

    inner class Holder : BaseEpoxyHolder() {
        val progress by bind<ContentLoadingProgressBar>(R.id.progress)
    }

}
