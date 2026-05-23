package com.ulyup.tier_list.dto

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class TierListDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val isPublic: Boolean,
    val createdAt: Instant,
)

@Serializable
data class TierListDetailDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val isPublic: Boolean,
    val createdAt: Instant,
    val items: List<ItemDto>,
)

@Serializable
data class CreateTierListRequest(
    val title: String,
    val isPublic: Boolean = false,
)

@Serializable
data class UpdateTierListRequest(
    val title: String,
)

@Serializable
data class UpdateVisibilityRequest(
    val isPublic: Boolean,
)