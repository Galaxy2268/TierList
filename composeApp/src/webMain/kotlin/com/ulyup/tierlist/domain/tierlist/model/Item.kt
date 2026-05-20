package com.ulyup.tierlist.domain.tierlist.model

import com.ulyup.tierlist.model.Tier

data class Item(
    val id: Int,
    val tierlistId: Int,
    val imageUrl: String,
    val tier: Tier?,
    val position: Int,
)
