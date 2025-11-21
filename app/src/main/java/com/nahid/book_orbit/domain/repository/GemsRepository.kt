package com.nahid.book_orbit.domain.repository

import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.data.remote.dto.Gems

interface GemsRepository {
    suspend fun getAllGems(): Results<List<Gems>>
    //suspend fun isGemsPurchased(userId: String): Boolean
}