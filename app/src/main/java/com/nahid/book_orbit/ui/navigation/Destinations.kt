package com.nahid.book_orbit.ui.navigation

import androidx.navigation.NavBackStackEntry
import com.nahid.book_orbit.data.remote.dto.Book
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
    data class BookDetails(val book: String?) : Destinations()

    @Serializable
    data class PDFScreen(val bookUrl: String?) : Destinations()

    @Serializable
    data object BuyGems : Destinations()
}