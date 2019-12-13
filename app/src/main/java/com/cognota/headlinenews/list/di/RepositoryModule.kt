package com.cognota.headlinenews.list.di

import com.cognota.headlinenews.list.data.ListDataContract
import com.cognota.headlinenews.list.data.ListRepository
import dagger.Binds
import dagger.Module


/**
 * All repository classes go here
 */
@Module
abstract class RepositoryModule {

    @Binds
    @ListScope
    abstract fun bindNewsRepository(listRepository: ListRepository): ListDataContract.Repository

}