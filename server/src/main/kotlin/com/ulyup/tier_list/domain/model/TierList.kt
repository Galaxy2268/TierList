package com.ulyup.tier_list.domain.model

import kotlin.time.Instant

data class TierList(
    val id: Int,
    val userId: Int,
    val title: String,
    val isPublic: Boolean,
    val createdAt: Instant,
)