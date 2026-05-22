package com.ulyup.tierlist.domain.tierlist.repository

import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.model.Tier

interface ItemRepository {
    suspend fun create(tierlistId: Int, bytes: ByteArray, filename: String): Item
    suspend fun delete(tierlistId: Int, itemId: Int)
    suspend fun move(tierlistId: Int, itemId: Int, tier: Tier?, position: Int): Item
}
