package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.entity.BookmarkEntity
import com.cognota.feed.commons.domain.BookmarkDTO
import com.cognota.feed.commons.domain.FeedDTO
import javax.inject.Inject

class BookmarkDTOMapper @Inject constructor() {

    fun toDTO(entity: BookmarkEntity): BookmarkDTO {

        return BookmarkDTO(
            categoryCode = entity.categoryCode,
            categoryIcon = entity.categoryIcon,
            categoryName = entity.categoryName,
            description = entity.description,
            id = entity.id,
            image = entity.image,
            link = entity.link,
            publishedDate = entity.pubDate,
            sourceCode = entity.sourceCode,
            sourceFavicon = entity.sourceFavicon,
            sourceName = entity.sourceName,
            sourceIcon = entity.sourceIcon,
            title = entity.title,
            updateDate = entity.updateDate
        )

    }

    fun fromFeedDto(feed: FeedDTO): BookmarkEntity {
        return BookmarkEntity(
            categoryCode = feed.category.code,
            categoryIcon = feed.category.icon,
            categoryName = feed.category.name,
            description = feed.description,
            id = feed.id,
            image = feed.image,
            link = feed.link,
            pubDate = feed.publishedDate,
            sourceCode = feed.source.code,
            sourceFavicon = feed.source.favicon,
            sourceName = feed.source.name,
            sourceIcon = feed.source.icon,
            title = feed.title,
            updateDate = feed.updatedDate
        )
    }

}