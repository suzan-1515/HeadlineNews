package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.FeedType
import javax.inject.Inject

class FeedDTOMapper @Inject constructor(
    private val sourceDTOMapper: SourceDTOMapper,
    private val categoryDTOMapper: CategoryDTOMapper
) {

    fun toDTO(
        entity: FeedWithSourcesEntity
    ): FeedDTO {
        return FeedDTO(
            id = entity.feed.id,
            title = entity.feed.title,
            category = categoryDTOMapper.toDTO(entity.category),
            description = entity.feed.description,
            image = entity.feed.image,
            link = entity.feed.link,
            publishedDate = entity.feed.pubDate,
            source = sourceDTOMapper.toDTO(entity.source),
            updatedDate = entity.feed.updateDate,
            type = FeedType.valueOf(entity.feed.type),
            page = entity.feed.page,
            parentId = entity.feed.parentId
        )

    }

}