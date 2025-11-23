package com.nahid.book_orbit.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.nahid.book_orbit.core.utils.Results
import com.nahid.book_orbit.core.utils.extension.getSpecificException
import com.nahid.book_orbit.data.remote.dto.Gems
import com.nahid.book_orbit.data.remote.dto.GemsTransaction
import com.nahid.book_orbit.domain.repository.GemsRepository
import kotlinx.coroutines.tasks.await

private const val TAG = "GemsRepositoryImpl"
class GemsRepositoryImpl(
    private val db: FirebaseFirestore
) : GemsRepository {
    override suspend fun getAllGems(): Results<List<Gems>> {
        return try {
            val querySnapshot = db.collection("gems")
                .get()
                .await()
            val gems = querySnapshot.documents.mapNotNull { document ->
                val gem = document.toObject(Gems::class.java)
                gem?.apply {
                    id = document.id
                }
            }
            Results.Success(gems)
        } catch (e: Exception) {
            Results.Error(e.getSpecificException())
        }
    }


    override suspend fun purchaseGems(uid: String, gemsId: String): Results<Boolean> {
        return try {
            val gemsDoc = db.collection("gems").document(gemsId).get().await()
            if (!gemsDoc.exists()) return Results.Error(Exception("Gems pack not found"))
            val gemsAmount = gemsDoc.getLong("gemsAmount") ?: 0L
            val walletRef = db.collection("wallet").document(uid)

            db.runTransaction { trx ->
                val snap = trx.get(walletRef)
                val currentGems = snap.getLong("gems") ?: 0L
                val newGems = currentGems + gemsAmount

                trx.set(walletRef, mapOf("gems" to newGems), SetOptions.merge())

                val txnRef = walletRef.collection("transactions").document()
                trx.set(
                    txnRef, mapOf(
                        "gemsAmount" to gemsAmount,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )
            }.await()

            Results.Success(true)
        } catch (e: Exception) {
            Results.Error(e.getSpecificException())
        }
    }


    // --------------------
    // Get total gems
    // --------------------
    override suspend fun getTotalGems(uid: String): Results<Long> {
        return try {
            Log.d(TAG, "getTotalGems: $uid")
            val snap = db.collection("wallet")
                .document(uid)
                .get()
                .await()

            Log.d(TAG, "getTotalGems: $uid ${snap.getLong("gems") ?: 0L}")
            Results.Success(snap.getLong("gems") ?: 0L)
        } catch (e: Exception) {
            Log.d(TAG, "getTotalGems: ${e.message}")
            Results.Error(e.getSpecificException())
        }
    }

    // --------------------
    // Get transaction history
    // --------------------
    override suspend fun getTransactionHistory(uid: String): Results<List<GemsTransaction>> {
        return try {
            Log.d(TAG, "getTransactionHistory: ")
            val transactions = db.collection("wallet").document(uid)
                .collection("transactions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(GemsTransaction::class.java)
            Log.d(TAG, "getTransactionHistory: $transactions")
            Results.Success(transactions)
        } catch (e: Exception) {
            Results.Error(e.getSpecificException())
        }
    }

}