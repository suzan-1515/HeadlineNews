package com.cognota.headlinenews.list.data.relation


import androidx.room.Embedded
import androidx.room.Relation
import com.cognota.headlinenews.list.data.entity.FeedEntity

data class FeedWithRelatedEntity(
    @Embedded
    val feed: FeedEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "related_feed"
    )
    val relatedFeed: List<FeedEntity>?

)