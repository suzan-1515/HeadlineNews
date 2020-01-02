package com.cognota.feed.commons.data.local.relation


import androidx.room.Embedded
import androidx.room.Relation
import com.cognota.feed.commons.data.local.entity.FeedEntity

data class FeedWithRelatedEntity(
    @Embedded
    val feed: FeedEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "related_feed"
    )
    val relatedFeed: List<FeedEntity>?
)