package com.cognota.feed.commons.domain

import java.io.Serializable

data class FeedWithRelatedFeedDTO(
    val feeds: FeedDTO,
    val feedWithRelatedFeeds: List<RelatedFeedDTO>
) : Serializable