package com.cognota.feed.commons.di

import android.content.SharedPreferences
import com.cognota.core.di.ModuleScope
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.data.SourceAndCategoryRepository
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.CategoryDTOMapper
import com.cognota.feed.commons.data.mapper.CategoryResponseMapper
import com.cognota.feed.commons.data.mapper.SourceDTOMapper
import com.cognota.feed.commons.data.mapper.SourceResponseMapper
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    /*Repository*/
    @Provides
    @ModuleScope
    fun sourceAndCategoryRepo(
        newsAPIService: NewsAPIService,
        newsDao: NewsDao,
        sharedPreferences: SharedPreferences,
        sourceResponseMapper: SourceResponseMapper,
        sourceDTOMapper: SourceDTOMapper,
        categoryResponseMapper: CategoryResponseMapper,
        categoryDTOMapper: CategoryDTOMapper
    ): SourceAndCategoryDataContract.Repository =
        SourceAndCategoryRepository(
            newsAPIService,
            newsDao,
            sharedPreferences,
            sourceResponseMapper,
            sourceDTOMapper,
            categoryResponseMapper,
            categoryDTOMapper
        )

}