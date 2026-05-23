package com.ulyup.tier_list.domain.tier_list.model

import com.ulyup.tier_list.model.Tier

data class TierListItem(
    val id: Int,
    val tierListId: Int,
    val imageUrl: String,
    val tier: Tier?,
    val position: Int,
)
