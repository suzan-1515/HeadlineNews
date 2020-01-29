package com.cognota.feed.commons.domain

data class PersonalisedFeedDTO(
    var trendingFeeds: List<FeedDTO>? = null,
    var tags: List<TagDTO>? = null,
    var sources: List<SourceDTO>? = null,
    var categories: List<CategoryDTO>? = null,
    var latestFeeds: List<FeedDTO>? = null,
    var dataType: PersonalisedFeedDataType
)