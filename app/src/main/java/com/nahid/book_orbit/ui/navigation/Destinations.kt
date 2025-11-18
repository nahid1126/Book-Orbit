package com.nahid.book_orbit.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    data object SplashScreen : Destinations()

    @Serializable
    data object LoginScreen : Destinations()

    @Serializable
    data object Home : Destinations()

    @Serializable
    data object Search : Destinations()


    @Serializable
    data object Books : Destinations()

    @Serializable
    data object Profile : Destinations()

    @Serializable
    data object BookDetails : Destinations()
}