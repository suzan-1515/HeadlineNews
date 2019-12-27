package com.cognota.feed.list.data.mapper

import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.list.data.entity.FeedEntity
import com.cognota.feed.list.data.relation.FeedWithRelatedEntity
import javax.inject.Inject

class FeedDTOMapper @Inject constructor() {

    fun toDTO(
        entity: FeedWithRelatedEntity
    ): FeedDTO = FeedDTO(
        category = entity.feed.category,
        description = entity.feed.description,
        id = entity.feed.id,
        image = entity.feed.image,
        link = entity.feed.link,
        publishedDate = entity.feed.pubDate,
        source = entity.feed.source,
        title = entity.feed.title,
        updatedDate = entity.feed.updateDate,
        uuid = entity.feed.uuid,
        type = FeedType.valueOf(entity.feed.type),
        relatedFeed = entity.relatedFeed?.map {
            toDTO(it)
        }
    )


    fun toDTO(entity: FeedEntity): FeedDTO {
        return FeedDTO(
            category = entity.category,
            description = entity.description,
            id = entity.id,
            image = entity.image,
            link = entity.link,
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