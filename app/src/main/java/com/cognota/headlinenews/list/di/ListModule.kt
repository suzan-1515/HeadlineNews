package com.cognota.headlinenews.list.di

import android.content.SharedPreferences
import com.cognota.headlinenews.commons.data.local.dao.NewsDao
import com.cognota.headlinenews.commons.data.remote.service.NewsAPIService
import com.cognota.headlinenews.list.data.ListDataContract
import com.cognota.headlinenews.list.data.ListRepository
import com.cognota.headlinenews.list.data.mapper.FeedDTOMapper
import com.cognota.headlinenews.list.data.mapper.FeedResponseMapper
import com.cognota.headlinenews.list.viewmodel.ListViewModelFactory
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module(includes = [NetworkModule::class, PersistanceModule::class])
class ListModule {

    /*ViewModel*/
    @Provides
    @ListScope
    fun listViewModelFactory(
        repository: ListDataContract.Repository
    ): ListViewModelFactory = ListViewModelFactory(repository)

    /*Repository*/
    @Provides
    @ListScope
    fun listRepo(
        newsAPIService: NewsAPIService,
        newsDao: NewsDao,
        sharedPreferences: SharedPreferences,
        feedResponseMapper: FeedResponseMapper,
        feedDTOMapper: FeedDTOMapper
    ): ListDataContract.Repository = ListRepository(
        newsAPIService,
        newsDao,
        sharedPreferences,
        feedResponseMapper,
        feedDTOMapper
    )

    @Provides
    @ListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}