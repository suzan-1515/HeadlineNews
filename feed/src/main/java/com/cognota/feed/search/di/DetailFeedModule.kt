package com.cognota.feed.search.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.detail.data.DetailFeedDataContract
import com.cognota.feed.detail.data.DetailFeedRepository
import com.cognota.feed.detail.viewmodel.DetailFeedViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class DetailFeedModule {

    /*ViewModel*/
    @Provides
    @FeatureScope
    fun detailViewModelFactory(
        repository: DetailFeedDataContract.Repository
    ): DetailFeedViewModelFactory =
        DetailFeedViewModelFactory(repository)

    /*Repository*/
    @Provides
    @FeatureScope
    fun detailFeedRepo(
        newsDao: NewsDao,
        feedDTOMapper: FeedDTOMapper
    ): DetailFeedDataContract.Repository = DetailFeedRepository(
        newsDao,
        feedDTOMapper
    )

}