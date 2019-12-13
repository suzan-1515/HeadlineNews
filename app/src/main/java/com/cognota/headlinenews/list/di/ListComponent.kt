package com.cognota.headlinenews.list.di

import android.content.SharedPreferences
import com.cognota.core.di.CoreComponent
import com.cognota.core.util.Scheduler
import com.cognota.headlinenews.commons.data.local.database.NewsDb
import com.cognota.headlinenews.commons.data.remote.service.NewsAPIService
import com.cognota.headlinenews.list.ListActivity
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ListScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [ListModule::class, AndroidSupportInjectionModule::class]
)
interface ListComponent {

    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating
        // an instance of ListComponent
        fun create(coreComponent: CoreComponent): ListComponent
    }

    fun inject(activity: ListActivity)

    fun schedular(): Scheduler

    fun sharedPrefs(): SharedPreferences

    fun newsService(): NewsAPIService

    fun newsDB(): NewsDb

    fun picasso(): Picasso

}
