package com.cognota.feed.commons.domain

import java.io.Serializable

data class SourceAndCategoryDTO(
    val source: List<SourceDTO>?,
    val category: List<CategoryDTO>?
) : Serializable