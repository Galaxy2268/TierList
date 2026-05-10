package com.ulyup.tierlist.dto

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class TierlistDto(
    val id: Int,
    val userId: Int,
    val ownerUsername: String,
    val title: String,
    val isPublic: Boolean,
    val createdAt: Instant,
    val itemCount: Int,
)

@Serializable
data class CreateTierlistRequest(
    val title: String,
    val isPublic: Boolean = false,
)

@Serializable
data class UpdateTierlistRequest(
    val title: String,
)

@Serializable
data class UpdateVisibilityRequest(
    val isPublic: Boolean,
)