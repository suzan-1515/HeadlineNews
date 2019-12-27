package com.cognota.feed.list.data.mapper

import com.cognota.core.data.persistence.converter.BaseConverter
import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.remote.model.FeedResponse
import com.cognota.feed.list.data.entity.FeedEntity
import com.cognota.feed.list.data.relation.FeedWithRelatedEntity
import javax.inject.Inject

class FeedResponseMapper @Inject constructor() {

    fun toWithRelatedEntities(response: FeedResponse, feedType: FeedType): FeedWithRelatedEntity {

        val feedEntity = toEntity(response, feedType)

        return FeedWithRelatedEntity(
            feed = feedEntity,
            relatedFeed = response.relatedFeedResponse?.map {
                FeedEntity(
                    author = response.author,
                    category = response.category,
                    content = response.content,
                    description = response.description,
                    encloserType = response.encloserType,
                    encloserUrl = response.encloserUrl,
                    fetchDate = BaseConverter().toOffsetDateTime(response.fetchDate),
                    id = response.id,
                    image = response.image,
                    link = response.link,
                    pubDate = BaseConverter().toOffsetDateTime(response.pubDate),
                    source = response.source,
                    title = response.title,
                    updateDate = BaseConverter().toOffsetDateTime(response.updateDate),
                    uuid = response.uuid,
                    relatedFeedId = feedEntity.id,
                    type = feedType.toString()
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
            fetchDate = BaseConverter().toOffsetDateTime(response.fetchDate),
            id = response.id,
            image = response.image,
            link = response.link,
            pubDate = BaseConverter().toOffsetDateTime(response.pubDate),
            source = response.source,
            title = response.title,
            updateDate = BaseConverter().toOffsetDateTime(response.updateDate),
            uuid = response.uuid,
            relatedFeedId = null,
            type = feedType.toString()
        )

    }


}