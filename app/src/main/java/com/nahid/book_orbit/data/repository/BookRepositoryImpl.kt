package com.nahid.book_orbit.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.domain.repository.BookRepository
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BookRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
):BookRepository {
    override suspend fun getAllBooks(): Results<List<Book>> {
        return try {
            val querySnapshot = db.collection("books")
                .get()
                .await()
            val books = querySnapshot.documents.mapNotNull { document ->
                val book = document.toObject(Book::class.java)
                book?.apply {
                    id = document.id
                }
            }
            Results.Success(books)
        } catch (e: Exception) {
            Results.Error(e)
        }
    }

    override suspend fun isBookPurchased(userId: String, bookId: String): Boolean =
        suspendCoroutine { cont ->
            db.collection("users")
                .document(userId)
                .collection("purchases")
                .document(bookId)
                .get()
                .addOnSuccessListener { cont.resume(it.exists()) }
                .addOnFailureListener { cont.resume(false) }
        }
}