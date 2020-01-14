package com.cognota.feed.commons.adapter

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.CarouselModel_

class IndicatorCarousel : CarouselModel_() {

    private val indicator =
        CirclePagerIndicatorDecoration()

    override fun bind(carousel: Carousel) {
        super.bind(carousel)

        carousel.addItemDecoration(indicator)
    }

    override fun unbind(carousel: Carousel) {
        super.unbind(carousel)

        carousel.removeItemDecoration(indicator)
    }
}