package com.cognota.feed.commons.di

import com.cognota.core.di.ModuleScope
import com.cognota.feed.commons.data.BookmarkDataContract
import com.cognota.feed.commons.viewmodel.BookmarkFeedViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    /*ViewModel*/
    @Provides
    @ModuleScope
    fun bookmarkViewModelFactory(
        repository: BookmarkDataContract.Repository
    ): BookmarkFeedViewModelFactory =
        BookmarkFeedViewModelFactory(repository)


}