package com.ulyup.tier_list.data.tier_list.repository

import com.ulyup.tier_list.data.tier_list.api.ItemApi
import com.ulyup.tier_list.data.tier_list.mapper.toDomain
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository
import com.ulyup.tier_list.dto.MoveItemRequest
import com.ulyup.tier_list.model.Tier

class ItemRepositoryImpl(
    private val itemApi: ItemApi,
) : ItemRepository {

    override suspend fun create(tierListId: Int, bytes: ByteArray, filename: String): TierListItem =
        itemApi.create(tierListId, bytes, filename).toDomain()

    override suspend fun delete(tierListId: Int, itemId: Int) {
        itemApi.delete(tierListId, itemId)
    }

    override suspend fun move(tierListId: Int, itemId: Int, tier: Tier?, position: Int): TierListItem =
        itemApi.move(tierListId, itemId, MoveItemRequest(tier = tier, position = position)).toDomain()
}
