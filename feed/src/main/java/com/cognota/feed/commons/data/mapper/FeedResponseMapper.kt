package com.cognota.feed.commons.data.mapper

import com.cognota.core.util.DateTimeUtil
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.domain.FeedType
import javax.inject.Inject

class FeedResponseMapper @Inject constructor() {

    fun toEntity(
        response: NewsFeedResponse.Feed,
        feedType: FeedType,
        page: Int = 1
    ): FeedEntity {

        return FeedEntity(
            author = response.author,
            category = response.category,
            content = response.content,
            description = response.description,
            id = response.id,
            image = response.image,
            link = response.link,
            pubDate = DateTimeUtil.toLocalDateTime(
                response.pubDate,
                DateTimeUtil.DEFAULT_SERVER_DATE_TIME_FORMAT
            ),
            source = response.source,
            title = response.title,
            uuid = response.uuid,
            type = feedType.toString(),
            enabled = true,
            parentId = null,
            page = page
        )


    }

    fun toEntity(
        response: NewsFeedResponse.Feed.Related,
        parentEntity: FeedEntity
    ): FeedEntity {

        return FeedEntity(
            author = response.author,
            category = response.category,
            content = response.content,
            description = response.description,
            id = response.id,
            image = response.image,
            link = response.link,
            pubDate = DateTimeUtil.toLocalDateTime(
                response.pubDate,
                DateTimeUtil.DEFAULT_SERVER_DATE_TIME_FORMAT
            ),
            source = response.source,
            title = response.title,
            uuid = response.uuid,
            type = parentEntity.type,
            enabled = true,
            parentId = parentEntity.id,
            page = parentEntity.page
        )

    }
}