package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.local.entity.RelatedFeedEntity
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import javax.inject.Inject

class FeedResponseMapper @Inject constructor() {

    fun toEntity(
        response: NewsFeedResponse.Feed,
        feedType: FeedType
    ): FeedEntity {

        return FeedEntity(
            author = response.author,
            category = response.category,
            content = response.content,
            description = response.description,
            encloserType = response.encloserType,
            encloserUrl = response.encloserUrl,
            fetchDate = response.fetchDate,
            id = response.id,
            image = response.image,
            link = response.link,
            pubDate = response.pubDate,
            source = response.source,
            title = response.title,
            updateDate = response.updateDate,
            uuid = response.uuid,
            type = feedType.toString()
        )


    }

    fun toEntity(
        response: NewsFeedResponse.Feed.Related,
        parentEntity: FeedEntity
    ): RelatedFeedEntity {

        return RelatedFeedEntity(
            author = response.author,
            category = response.category,
            content = response.content,
            description = response.description,
            encloserType = response.encloserType,
            encloserUrl = response.encloserUrl,
            fetchDate = response.fetchDate,
            id = response.id,
            image = response.image,
            link = response.link,
            pubDate = response.pubDate,
            source = response.source,
            title = response.title,
            updateDate = response.updateDate,
            uuid = response.uuid,
            type = parentEntity.type,
            parentId = parentEntity.id
        )

    }


}