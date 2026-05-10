package com.ulyup.tierlist.domain.model

import com.ulyup.tierlist.model.Tier

data class TierlistItem(
    val id: Int,
    val tierlistId: Int,
    val imageUrl: String,
    val tier: Tier?,
    val position: Int,
)