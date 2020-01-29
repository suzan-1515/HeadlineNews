package com.cognota.feed.commons.data

import com.cognota.core.vo.Resource
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.SourceAndCategoryDTO
import com.cognota.feed.commons.domain.SourceDTO
import kotlinx.coroutines.flow.Flow

interface SourceAndCategoryDataContract {
    interface Repository {

        suspend fun getSourcesAndCategories(): Flow<Resource<SourceAndCategoryDTO?>>

        suspend fun getSources(): Flow<List<SourceDTO>?>

        suspend fun getCategories(): Flow<List<CategoryDTO>?>

    }
}