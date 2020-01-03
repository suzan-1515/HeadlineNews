package com.cognota.feed.commons.data.local.relation


import androidx.room.Embedded
import androidx.room.Relation
import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.local.entity.RelatedFeedEntity
import com.cognota.feed.commons.data.local.entity.SourceEntity

data class RelatedFeedWithSourcesEntity(
    @Embedded
    val feed: RelatedFeedEntity,
    @Relation(
        parentColumn = "parent_id",
        entityColumn = "id",
        entity = FeedEntity::class
    )
    val parentFeed: FeedWithSourcesEntity,
    @Relation(
        parentColumn = "source_code",
        entityColumn = "code",
        entity = SourceEntity::class
    )
    val source: SourceEntity,
    @Relation(
        parentColumn = "category_code",
        entityColumn = "code",
        entity = CategoryEntity::class
    )
    val category: CategoryEntity
)