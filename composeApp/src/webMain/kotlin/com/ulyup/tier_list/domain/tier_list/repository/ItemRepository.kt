package com.ulyup.tier_list.domain.tier_list.repository

import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.model.Tier

interface ItemRepository {
    suspend fun create(tierListId: Int, bytes: ByteArray, filename: String): TierListItem
    suspend fun delete(tierListId: Int, itemId: Int)
    suspend fun move(tierListId: Int, itemId: Int, tier: Tier?, position: Int): TierListItem
}
