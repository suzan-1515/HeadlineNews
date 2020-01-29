package com.cognota.feed.category.di

import android.content.SharedPreferences
import com.cognota.core.di.FeatureScope
import com.cognota.feed.category.data.CategoryFeedDataContract
import com.cognota.feed.category.data.CategoryFeedRepository
import com.cognota.feed.category.viewmodel.CategoriesFeedViewModelFactory
import com.cognota.feed.category.viewmodel.CategoryFeedViewModelFactory
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.data.mapper.FeedResponseMapper
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import dagger.Module
import dagger.Provides

@Module
class CategoryFeedModule {

    /*ViewModel*/
    @Provides
    fun categoryViewModelFactory(
        repository: CategoryFeedDataContract.Repository
    ): CategoryFeedViewModelFactory =
        CategoryFeedViewModelFactory(
            repository
        )

    /*ViewModel*/
    @Provides
    @FeatureScope
    fun categoriesViewModelFactory(
        repository: SourceAndCategoryDataContract.Repository
    ): CategoriesFeedViewModelFactory =
        CategoriesFeedViewModelFactory(
            repository
        )

    /*Repository*/
    @Provides
    @FeatureScope
    fun categoryRepo(
        newsAPIService: NewsAPIService,
        newsDao: NewsDao,
        sharedPreferences: SharedPreferences,
        sourceAndCategoryRepo: SourceAndCategoryDataContract.Repository,
        feedResponseMapper: FeedResponseMapper,
        feedDTOMapper: FeedDTOMapper
    ): CategoryFeedDataContract.Repository =
        CategoryFeedRepository(
            newsAPIService,
            newsDao,
            sharedPreferences,
            sourceAndCategoryRepo,
            feedResponseMapper,
            feedDTOMapper
        )

}