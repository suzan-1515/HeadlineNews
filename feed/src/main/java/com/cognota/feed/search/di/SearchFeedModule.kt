package com.cognota.feed.search.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.TagDTOMapper
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.search.data.SearchFeedDataContract
import com.cognota.feed.search.data.SearchFeedRepository
import com.cognota.feed.search.viewmodel.SearchFeedViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SearchFeedModule {

    /*ViewModel*/
    @Provides
    @FeatureScope
    fun searchViewModelFactory(
        searchFeedRepo: SearchFeedDataContract.Repository
    ): SearchFeedViewModelFactory =
        SearchFeedViewModelFactory(searchFeedRepo)

    /*Repository*/
    @Provides
    @FeatureScope
    fun searchFeedRepo(
        newsAPIService: NewsAPIService,
        newsDao: NewsDao,
        sourceAndCategoryRepo: SourceAndCategoryDataContract.Repository,
        tagDTOMapper: TagDTOMapper
    ): SearchFeedDataContract.Repository =
        SearchFeedRepository(
            newsAPIService,
            newsDao,
            sourceAndCategoryRepo,
            tagDTOMapper
        )

}