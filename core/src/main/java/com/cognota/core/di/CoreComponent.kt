package com.cognota.core.di

import android.content.Context
import android.content.SharedPreferences
import com.cognota.core.App
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        StorageModule::class,
        ImageModule::class,
        AndroidSupportInjectionModule::class]
)
interface CoreComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): CoreComponent.Builder

        fun build(): CoreComponent
    }

    fun context(): Context

    fun retrofit(): Retrofit

    fun picasso(): Picasso

    fun sharedPreferences(): SharedPreferences

}