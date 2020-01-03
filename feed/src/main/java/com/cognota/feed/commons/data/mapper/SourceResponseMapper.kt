package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.entity.SourceEntity
import com.cognota.feed.commons.data.remote.model.SourceResponse
import javax.inject.Inject

class SourceResponseMapper @Inject constructor() {

    fun toEntity(response: SourceResponse.Source): SourceEntity {

        return SourceEntity(
            id = response.id,
            code = response.code,
            favicon = response.favicon,
            icon = response.icon,
            name = response.name,
            priority = response.priority
        )

    }


}