package com.cognota.feed.commons.domain

import java.io.Serializable

data class PersonalisedFeedDTO(
    val feeds: List<FeedWithRelatedFeedDTO>?,
    val sources: List<SourceDTO>?,
    val categories: List<CategoryDTO>?
) : Serializable