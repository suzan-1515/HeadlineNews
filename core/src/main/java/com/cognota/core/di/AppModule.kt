package com.cognota.core.di

import android.content.Context
import com.cognota.core.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesContext(app: App): Context {
        return app.applicationContext
    }

}