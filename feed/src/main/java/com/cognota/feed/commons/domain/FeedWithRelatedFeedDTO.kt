package com.cognota.feed.commons.domain

import java.io.Serializable

data class FeedWithRelatedFeedDTO(
    val feed: FeedDTO,
    val feedWithRelatedFeeds: List<RelatedFeedDTO>
) : Serializable