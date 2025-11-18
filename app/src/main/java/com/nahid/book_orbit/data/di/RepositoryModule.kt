package com.nahid.book_orbit.data.di

import com.nahid.book_orbit.data.repository.AuthRepositoryImpl
import com.nahid.book_orbit.data.repository.BookRepositoryImpl
import com.nahid.book_orbit.domain.repository.AuthRepository
import com.nahid.book_orbit.domain.repository.BookRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory<BookRepository> { BookRepositoryImpl(get(), get()) }
}
