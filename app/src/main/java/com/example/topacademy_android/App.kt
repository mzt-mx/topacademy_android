package com.example.topacademy_android

import android.app.Application
import com.example.topacademy_android.di.networkModule
import com.example.topacademy_android.di.repositoryModule
import com.example.topacademy_android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}

