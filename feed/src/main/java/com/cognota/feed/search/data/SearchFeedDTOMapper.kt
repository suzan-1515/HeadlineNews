package com.cognota.feed.search.data

import com.cognota.core.util.DateTimeUtil
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.FeedType
import com.cognota.feed.commons.domain.SourceDTO
import javax.inject.Inject

class SearchFeedDTOMapper @Inject constructor() {

    fun toDTO(
        hit: SearchFeedResponse.Hits.Hit,
        sourceDTO: SourceDTO,
        categoryDTO: CategoryDTO
    ): FeedDTO {
        return FeedDTO(
            id = hit.id,
            description = hit.source.description,
            image = hit.source.image,
            link = hit.source.link,
            publishedDate = hit.source.pubDate?.let {
                DateTimeUtil.toLocalDateTime(
                    it,
                    DateTimeUtil.DEFAULT_DATE_TIME_FORMAT
                )
            },
            title = hit.source.title,
            type = FeedType.SEARCH,
            source = sourceDTO,
            category = categoryDTO,
            page = 1
        )

    }

}