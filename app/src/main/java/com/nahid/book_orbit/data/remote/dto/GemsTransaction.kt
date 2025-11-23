package com.nahid.book_orbit.data.remote.dto

import com.google.firebase.Timestamp

data class GemsTransaction(
    val gemsAmount: Int = 0,
    val timestamp: Timestamp? = null
)
