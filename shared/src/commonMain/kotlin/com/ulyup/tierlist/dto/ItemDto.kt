package com.ulyup.tierlist.dto

import com.ulyup.tierlist.model.Tier
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int,
    val tierlistId: Int,
    val name: String,
    val imageUrl: String?,
    val tier: Tier?,
    val notes: String?,
    val position: Int,
)

@Serializable
data class CreateItemRequest(
    val name: String,
    val imageUrl: String? = null,
    val tier: Tier? = null,
    val notes: String? = null,
    val position: Int? = null,
)

@Serializable
data class UpdateItemRequest(
    val name: String,
    val imageUrl: String? = null,
    val position: Int,
)

@Serializable
data class AssignTierRequest(
    val tier: Tier?,
)

@Serializable
data class UpdateNotesRequest(
    val notes: String?,
)
