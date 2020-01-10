package com.cognota.feed.list.adapter


import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelView
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class FlowCarousel(context: Context) : Carousel(context) {

    override fun createLayoutManager(): LayoutManager {
        val flowLayoutManager = FlowLayoutManager()
        flowLayoutManager.setAlignment(Alignment.LEFT)
        flowLayoutManager.isAutoMeasureEnabled = true
        return flowLayoutManager
    }

}