package com.ulyup.tier_list.domain.repository

import com.ulyup.tier_list.domain.model.TierListItem
import com.ulyup.tier_list.model.Tier

interface ItemRepository {
    suspend fun create(tierListId: Int, userId: Int, imageUrl: String): TierListItem?
    suspend fun findById(id: Int): TierListItem?
    suspend fun findByTierListId(tierListId: Int): List<TierListItem>
    suspend fun update(id: Int, tierListId: Int, userId: Int, imageUrl: String): TierListItem?
    suspend fun delete(id: Int, tierListId: Int, userId: Int): TierListItem?
    suspend fun deleteByTierListId(tierListId: Int, userId: Int): List<TierListItem>?
    suspend fun move(id: Int, tierListId: Int, userId: Int, newTier: Tier?, newPosition: Int): TierListItem?
}
