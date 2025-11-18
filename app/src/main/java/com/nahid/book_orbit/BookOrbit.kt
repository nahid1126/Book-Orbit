package com.nahid.book_orbit

import android.app.Application
import com.nahid.book_orbit.data.di.appModule
import com.nahid.book_orbit.data.di.firebaseModule
import com.nahid.book_orbit.data.di.networkModule
import com.nahid.book_orbit.data.di.repositoryModule
import com.nahid.book_orbit.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookOrbit : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookOrbit)
            modules(
                appModule,
                networkModule,
                repositoryModule,
                viewModelModule,
                firebaseModule
            )
        }
    }
}