package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.data.remote.model.FeedResponse
import javax.inject.Inject

class FeedResponseMapper @Inject constructor() {

    fun toWithRelatedEntities(response: FeedResponse, feedType: FeedType): FeedWithRelatedEntity {

        val feedEntity = toEntity(response, feedType)

        return FeedWithRelatedEntity(
            feed = feedEntity,
            relatedFeed = response.relatedFeedResponse?.map {
                FeedEntity(
                    author = it.author,
                    category = it.category,
                    content = it.content,
                    description = it.description,
                    encloserType = it.encloserType,
                    encloserUrl = it.encloserUrl,
                    fetchDate = it.fetchDate,
                    id = it.id,
                    image = it.image,
                    link = it.link,
                    pubDate = it.pubDate,
                    source = it.source,
                    title = it.title,
                    updateDate = it.updateDate,
                    uuid = it.uuid,
                    relatedFeedId = feedEntity.id,
                    type = feedEntity.type
                )
            })
    }

    fun toEntity(response: FeedResponse, feedType: FeedType): FeedEntity {

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
            relatedFeedId = null,
            type = feedType.toString()
        )

    }


}