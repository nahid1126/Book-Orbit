package com.nahid.book_orbit.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.data.remote.dto.Book
import com.nahid.book_orbit.domain.repository.BookRepository
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "BookRepositoryImpl"
class BookRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : BookRepository {
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
                .collection("purchase")
                .document(bookId)
                .get()
                .addOnSuccessListener { cont.resume(it.exists()) }
                .addOnFailureListener { cont.resume(false) }
        }

    override suspend fun purchasedBook(
        uId: String?,
        data: HashMap<String, Any?>
    ): Results<Boolean> {
        val walletRef = db.collection("wallet").document(uId.toString())
        val purchaseRef = db.collection("purchase").document(uId.toString())

        return try {
            db.runTransaction { trx ->
                val walletSnap = trx.get(walletRef)
                val currentGems = walletSnap.getLong("gems") ?: 0L

                val bookPrice = (data["price"] as? Long) ?: 0L
                Log.d(TAG, "purchasedBook: $currentGems, $bookPrice")
                if (currentGems < bookPrice) {
                    throw Exception("Not enough gems")
                }
                val newGems = currentGems - bookPrice
                trx.set(walletRef, mapOf("gems" to newGems), SetOptions.merge())

                trx.set(purchaseRef, data, SetOptions.merge())

                null
            }.await()

            Results.Success(true)

        } catch (e: Exception) {
            Results.Error(e)
        }
    }

    override suspend fun getPurchasedBooks(uid: String): Results<List<Book>> {
        return try {
            val purchaseSnap = db.collection("purchase")
                .document(uid)
                .get()
                .await()

            if (!purchaseSnap.exists()) return Results.Success(emptyList())

            val bookList = mutableListOf<Book>()

            val nestedBooksMap = purchaseSnap.get("books") as? Map<String, Map<String, Any>>
            if (nestedBooksMap != null) {
                for ((bookId, bookData) in nestedBooksMap) {
                    val purchased = bookData["purchased"] as? Boolean ?: false
                    if (!purchased) continue

                    val bookSnap = db.collection("books").document(bookId).get().await()
                    if (bookSnap.exists()) {
                        val book = bookSnap.toObject(Book::class.java)
                        if (book != null) {
                            val price = (bookData["price"] as? Long)?.toInt() ?: 0
                            bookList.add(book.copy(id = bookId, price = price))
                        }
                    }
                }
            } else {
                for ((key, value) in purchaseSnap.data ?: emptyMap<String, Any>()) {
                    if (!key.startsWith("books.")) continue

                    val bookId = key.removePrefix("books.")
                    val bookData = value as? Map<String, Any> ?: continue
                    val purchased = bookData["purchased"] as? Boolean ?: false
                    if (!purchased) continue

                    val bookSnap = db.collection("books").document(bookId).get().await()
                    if (bookSnap.exists()) {
                        val book = bookSnap.toObject(Book::class.java)
                        if (book != null) {
                            val price = (bookData["price"] as? Long)?.toInt() ?: 0
                            bookList.add(book.copy(id = bookId, price = price))
                        }
                    }
                }
            }

            Log.d(TAG, "Final purchased bookList: $bookList")
            Results.Success(bookList)

        } catch (e: Exception) {
            Log.e(TAG, "getPurchasedBooks failed", e)
            Results.Error(Exception(e.message ?: "Failed to load purchased books"))
        }
    }






    override suspend fun deductGems(uid: String, cost: Int): Results<Boolean> {
        return try {
            db.collection("wallet")
                .document(uid)
                .update("gems", FieldValue.increment(-cost.toLong()))
                .await()

            Results.Success(true)
        } catch (e: Exception) {
            Results.Error(Exception(e.message ?: "Failed to deduct gems"))
        }
    }


}