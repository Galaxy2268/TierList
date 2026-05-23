package com.ulyup.tier_list.domain.tier_list.model

data class TierList(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val isPublic: Boolean,
    val createdAt: Long,
)
