package com.ulyup.tierlist.domain.repository

import com.ulyup.tierlist.domain.model.TierlistItem
import com.ulyup.tierlist.model.Tier

interface ItemRepository {
    suspend fun create(tierlistId: Int, imageUrl: String, tier: Tier?, position: Int): TierlistItem
    suspend fun findById(id: Int): TierlistItem?
    suspend fun findByTierlistId(tierlistId: Int): List<TierlistItem>
    suspend fun update(id: Int, tierlistId: Int, imageUrl: String): TierlistItem?
    suspend fun delete(id: Int, tierlistId: Int): Boolean
    suspend fun move(id: Int, tierlistId: Int, newTier: Tier?, newPosition: Int): TierlistItem?
    suspend fun nextPosition(tierlistId: Int, tier: Tier?): Int
}