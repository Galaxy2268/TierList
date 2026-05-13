package com.ulyup.tierlist.dto

import com.ulyup.tierlist.model.Tier
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int,
    val tierlistId: Int,
    val imageUrl: String,
    val tier: Tier?,
    val position: Int,
)

@Serializable
data class CreateItemRequest(
    val imageUrl: String,
    val tier: Tier? = null,
)

@Serializable
data class UpdateItemRequest(
    val imageUrl: String,
)