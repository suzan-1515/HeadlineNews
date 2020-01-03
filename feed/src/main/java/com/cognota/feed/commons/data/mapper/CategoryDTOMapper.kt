package com.cognota.feed.commons.data.mapper

import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.domain.CategoryDTO
import javax.inject.Inject

class CategoryDTOMapper @Inject constructor() {

    fun toDTO(entity: CategoryEntity): CategoryDTO {

        return CategoryDTO(
            id = entity.id,
            code = entity.code,
            enable = entity.enable,
            icon = entity.icon,
            name = entity.name,
            nameNp = entity.nameNp,
            priority = entity.priority
        )

    }

    fun toEntity(entity: CategoryDTO): CategoryEntity {

        return CategoryEntity(
            id = entity.id,
            code = entity.code,
            enable = entity.enable,
            icon = entity.icon,
            name = entity.name,
            nameNp = entity.nameNp,
            priority = entity.priority
        )

    }


}