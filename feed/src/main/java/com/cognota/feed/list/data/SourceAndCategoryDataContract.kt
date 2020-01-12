package com.cognota.feed.list.data

import com.cognota.core.repository.Resource
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.SourceAndCategoryDTO
import com.cognota.feed.commons.domain.SourceDTO
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface SourceAndCategoryDataContract {
    interface Repository {

        suspend fun getSourcesAndCategories(): Deferred<Resource<SourceAndCategoryDTO?>>

        suspend fun getSourcesStream(): Flow<List<SourceDTO>?>

        suspend fun getCategoriesStream(): Flow<List<CategoryDTO>?>

    }
}