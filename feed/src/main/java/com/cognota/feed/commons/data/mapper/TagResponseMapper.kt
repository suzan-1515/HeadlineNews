package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.entity.TagEntity
import javax.inject.Inject

class TagResponseMapper @Inject constructor() {

    fun toEntity(response: String): TagEntity {

        return TagEntity(
            title = response,
            icon = null
        )

    }


}