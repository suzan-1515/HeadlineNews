package com.cognota.feed.commons.data.local.relation


import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.local.entity.SourceEntity

data class SourceWithCategoryEntity(
    val source: List<SourceEntity>?,
    val category: List<CategoryEntity>?
)