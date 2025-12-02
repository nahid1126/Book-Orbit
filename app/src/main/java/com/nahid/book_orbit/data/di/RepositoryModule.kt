package com.nahid.book_orbit.data.di

import com.nahid.book_orbit.data.repository.AuthRepositoryImpl
import com.nahid.book_orbit.data.repository.BillingRepositoryImpl
import com.nahid.book_orbit.data.repository.BookRepositoryImpl
import com.nahid.book_orbit.data.repository.GemsRepositoryImpl
import com.nahid.book_orbit.domain.repository.AuthRepository
import com.nahid.book_orbit.domain.repository.BillingRepository
import com.nahid.book_orbit.domain.repository.BookRepository
import com.nahid.book_orbit.domain.repository.GemsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<AuthRepository> { AuthRepositoryImpl(get(),get()) }
    factory<BookRepository> { BookRepositoryImpl(get(), get()) }
    factory<GemsRepository> { GemsRepositoryImpl(get()) }
    factory<BillingRepository> { BillingRepositoryImpl(androidContext(), get(), get()) }
}
