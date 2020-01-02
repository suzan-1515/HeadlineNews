package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.domain.FeedDTO
import javax.inject.Inject

class FeedDTOMapper @Inject constructor() {

    fun toDTO(
        entity: FeedWithRelatedEntity
    ): FeedDTO {
        return FeedDTO(
            id = entity.feed.id,
            title = entity.feed.title,
            category = entity.feed.category,
            description = entity.feed.description,
            image = entity.feed.image,
            link = entity.feed.link,
            publishedDate = entity.feed.pubDate,
            source = entity.feed.source,
            updatedDate = entity.feed.updateDate,
            uuid = entity.feed.uuid,
            type = FeedType.valueOf(entity.feed.type),
            relatedFeed = entity.relatedFeed?.map {
                toDTO(it)
            }
        )

    }


    fun toDTO(entity: FeedEntity): FeedDTO {
        return FeedDTO(
            category = entity.category,
            description = entity.description,
            id = entity.id,
            link = entity.link,
            image = entity.image,
            publishedDate = entity.pubDate,
            source = entity.source,
            title = entity.title,
            updatedDate = entity.updateDate,
            uuid = entity.uuid,
            type = FeedType.valueOf(entity.type),
            relatedFeed = null
        )
    }

}