package com.nahid.book_orbit.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.nahid.book_orbit.core.utils.Results

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Results<FirebaseUser?>
    suspend fun getTotalGems(): Results<Long>
}