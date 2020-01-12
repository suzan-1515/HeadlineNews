package com.cognota.feed.list.di

import android.content.SharedPreferences
import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.data.mapper.FeedResponseMapper
import com.cognota.feed.commons.data.mapper.TagDTOMapper
import com.cognota.feed.commons.data.mapper.TagResponseMapper
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.list.data.PersonalizedFeedDataContract
import com.cognota.feed.list.data.PersonalizedFeedRepository
import com.cognota.feed.list.data.SourceAndCategoryDataContract
import com.cognota.feed.list.viewmodel.PersonalizedFeedViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PersonalizedFeedModule {

    /*ViewModel*/
    @Provides
    @FeatureScope
    fun listViewModelFactory(
        repository: PersonalizedFeedDataContract.Repository,
        sourceAndCategoryRepository: SourceAndCategoryDataContract.Repository
    ): PersonalizedFeedViewModelFactory =
        PersonalizedFeedViewModelFactory(repository, sourceAndCategoryRepository)

    /*Repository*/
    @Provides
    @FeatureScope
    fun personalizedFeedRepo(
        newsAPIService: NewsAPIService,
        newsDao: NewsDao,
        sharedPreferences: SharedPreferences,
        sourceAndCategoryRepository: SourceAndCategoryDataContract.Repository,
        feedResponseMapper: FeedResponseMapper,
        feedDTOMapper: FeedDTOMapper,
        tagResponseMapper: TagResponseMapper,
        tagDTOMapper: TagDTOMapper
    ): PersonalizedFeedDataContract.Repository = PersonalizedFeedRepository(
        newsAPIService,
        newsDao,
        sharedPreferences,
        sourceAndCategoryRepository,
        feedResponseMapper,
        feedDTOMapper,
        tagResponseMapper,
        tagDTOMapper
    )

}