package com.cognota.core.di

import android.content.Context
import com.cognota.core.application.CoreApp
import com.cognota.core.util.AppScheduler
import com.cognota.core.util.Scheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesContext(app: CoreApp): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun scheduler(): Scheduler {
        return AppScheduler()
    }

}