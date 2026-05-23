package com.ulyup.tier_list.dto

import com.ulyup.tier_list.model.Tier
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int,
    val tierListId: Int,
    val imageUrl: String,
    val tier: Tier?,
    val position: Int,
)

@Serializable
data class CreateItemRequest(
    val imageUrl: String,
)

@Serializable
data class UpdateItemRequest(
    val imageUrl: String,
)

@Serializable
data class MoveItemRequest(
    val tier: Tier?,
    val position: Int,
)