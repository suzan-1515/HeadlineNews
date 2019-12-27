package com.cognota.feed.list.data.relation


import androidx.room.Embedded
import androidx.room.Relation
import com.cognota.feed.list.data.entity.FeedEntity

data class FeedWithRelatedEntity(
    @Embedded
    val feed: FeedEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "related_feed"
    )
    val relatedFeed: List<FeedEntity>?

)