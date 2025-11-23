package com.nahid.book_orbit.domain.repository

import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book

interface BookRepository {
    suspend fun getAllBooks(): Results<List<Book>>
    suspend fun isBookPurchased(userId: String, bookId: String): Boolean
    suspend fun purchasedBook(uId: String?, data: HashMap<String, Any?>): Results<Boolean>
    suspend fun getPurchasedBooks(uid: String): Results<List<Book>>
    suspend fun deductGems(uid: String, cost: Int): Results<Boolean>
    suspend fun getTotalGems(uid: String): Results<Long>

}