package com.cognota.core.di

import android.content.Context
import com.cognota.core.BuildConfig
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ImageModule {

    @Provides
    @Singleton
    fun providesOkhttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @Provides
    @Singleton
    fun providesPicasso(context: Context, okHttp3Downloader: OkHttp3Downloader): Picasso {
        return Picasso.Builder(context)
            .loggingEnabled(BuildConfig.DEBUG)
            .downloader(okHttp3Downloader)
            .build()
    }
}