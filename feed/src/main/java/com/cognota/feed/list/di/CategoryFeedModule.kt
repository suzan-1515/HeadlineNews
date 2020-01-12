package com.cognota.feed.list.di

import android.content.SharedPreferences
import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.data.mapper.FeedResponseMapper
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.list.data.CategoryFeedDataContract
import com.cognota.feed.list.data.CategoryFeedRepository
import com.cognota.feed.list.data.SourceAndCategoryDataContract
import com.cognota.feed.list.viewmodel.CategoriesFeedViewModelFactory
import com.cognota.feed.list.viewmodel.CategoryFeedViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class CategoryFeedModule {

    /*ViewModel*/
    @Provides
    @FeatureScope
    fun categoryViewModelFactory(
        repository: CategoryFeedDataContract.Repository
    ): CategoryFeedViewModelFactory = CategoryFeedViewModelFactory(repository)

    /*ViewModel*/
    @Provides
    @FeatureScope
    fun categoriesViewModelFactory(
        repository: SourceAndCategoryDataContract.Repository
    ): CategoriesFeedViewModelFactory = CategoriesFeedViewModelFactory(repository)

    /*Repository*/
    @Provides
    @FeatureScope
    fun categoryRepo(
        newsAPIService: NewsAPIService,
        newsDao: NewsDao,
        sharedPreferences: SharedPreferences,
        feedResponseMapper: FeedResponseMapper,
        feedDTOMapper: FeedDTOMapper
    ): CategoryFeedDataContract.Repository = CategoryFeedRepository(
        newsAPIService,
        newsDao,
        sharedPreferences,
        feedResponseMapper,
        feedDTOMapper
    )

}