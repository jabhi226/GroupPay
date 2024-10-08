package com.example.grouppay

import android.app.Application
import com.example.grouppay.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GroupPayApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(androidContext = this@GroupPayApplication)
            modules(
                appModule
            )
        }
    }
}