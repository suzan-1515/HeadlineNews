package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.entity.SourceEntity
import com.cognota.feed.commons.domain.SourceDTO
import javax.inject.Inject

class SourceDTOMapper @Inject constructor() {

    fun toDTO(entity: SourceEntity): SourceDTO {

        return SourceDTO(
            id = entity.id,
            code = entity.code,
            favicon = entity.favicon,
            icon = entity.icon,
            name = entity.name,
            priority = entity.priority
        )

    }

    fun toEntity(entity: SourceDTO): SourceEntity {

        return SourceEntity(
            id = entity.id,
            code = entity.code,
            favicon = entity.favicon,
            icon = entity.icon,
            name = entity.name,
            priority = entity.priority
        )

    }


}