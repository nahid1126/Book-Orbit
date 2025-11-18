package com.nahid.book_orbit.data.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.nahid.book_orbit.core.utils.AppConstants
import com.nahid.book_orbit.data.local.AppPreference
import org.koin.dsl.module

val appModule = module {
    single {
        PreferenceDataStoreFactory.create {
            get<Context>().preferencesDataStoreFile(AppConstants.PREF_NAME)
        }
    }

    single {
        AppPreference(get())
    }
}