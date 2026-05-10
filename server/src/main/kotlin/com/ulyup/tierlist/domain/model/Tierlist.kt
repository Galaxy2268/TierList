package com.ulyup.tierlist.domain.model

import kotlin.time.Instant

data class Tierlist(
    val id: Int,
    val userId: Int,
    val title: String,
    val isPublic: Boolean,
    val createdAt: Instant,
)