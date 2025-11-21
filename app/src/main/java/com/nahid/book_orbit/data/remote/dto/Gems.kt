package com.nahid.book_orbit.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Gems(var id: String = "", val gemsAmount: Int = 0, val usd: Int = 0)
