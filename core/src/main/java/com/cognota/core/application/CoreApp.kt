package com.cognota.core.application

import android.content.Context
import androidx.multidex.MultiDex
import com.cognota.core.BuildConfig
import com.cognota.core.di.CoreComponent
import com.cognota.core.di.DaggerCoreComponent
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

open class CoreApp : DaggerApplication() {

    lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        initStetho()
        initTimber()
        AndroidThreeTen.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        coreComponent = DaggerCoreComponent.builder().application(this).build()
        return coreComponent
    }


    private fun initStetho() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}