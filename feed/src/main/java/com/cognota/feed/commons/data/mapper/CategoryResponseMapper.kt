package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.remote.model.SourceResponse
import javax.inject.Inject

class CategoryResponseMapper @Inject constructor() {

    fun toEntity(response: SourceResponse.Category): CategoryEntity {

        return CategoryEntity(
            id = response.id,
            code = response.code,
            enable = response.enable,
            icon = response.icon,
            name = response.name,
            nameNp = response.nameNp,
            priority = response.priority
        )

    }


}