package com.nahid.book_orbit.domain.repository

import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.data.remote.dto.Gems

interface GemsRepository {
    suspend fun getAllGems(): Results<List<Gems>>
    suspend fun purchaseGems(uid: String, gemsId: String): Results<Boolean>
    suspend fun purchaseBook(uid: String, bookId: String): Results<Boolean>
    suspend fun getTransactionHistory(uid: String): Results<List<Map<String, Any>>>
    //suspend fun isGemsPurchased(userId: String): Boolean
}