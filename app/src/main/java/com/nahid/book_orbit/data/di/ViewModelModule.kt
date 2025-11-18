package com.nahid.book_orbit.data.di

import com.nahid.book_orbit.ui.presentation.home.HomeScreenViewModel
import com.nahid.book_orbit.ui.presentation.main.MainViewModel
import com.nahid.book_orbit.ui.presentation.welcome.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeScreenViewModel()
    }
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        MainViewModel(get(), get())
    }
}