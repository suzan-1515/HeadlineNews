package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.entity.TagEntity
import com.cognota.feed.commons.domain.TagDTO
import javax.inject.Inject

class TagDTOMapper @Inject constructor() {

    fun toDTO(entity: TagEntity): TagDTO {

        return TagDTO(
            icon = entity.icon,
            title = entity.title
        )

    }

    fun toEntity(entity: TagDTO): TagEntity {

        return TagEntity(
            icon = entity.icon,
            title = entity.title
        )

    }


}