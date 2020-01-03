package com.cognota.feed.commons.data.local.relation


import androidx.room.Embedded
import androidx.room.Relation
import com.cognota.feed.commons.data.local.entity.RelatedFeedEntity

data class FeedWithRelatedEntity(
    @Embedded
    val feed: FeedWithSourcesEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "parent_id",
        entity = RelatedFeedEntity::class
    )
    val relatedFeeds: List<RelatedFeedWithSourcesEntity>?
)