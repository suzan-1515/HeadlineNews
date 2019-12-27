package com.cognota.feed.commons.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.list.data.ListDataContract
import com.cognota.feed.list.data.ListRepository
import dagger.Binds
import dagger.Module


/**
 * All repository classes go here
 */
@Module
abstract class RepositoryModule {

    @Binds
    @FeatureScope
    abstract fun bindNewsRepository(listRepository: ListRepository): ListDataContract.Repository

}