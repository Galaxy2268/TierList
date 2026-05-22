package com.ulyup.tierlist.data.tierlist.repository

import com.ulyup.tierlist.data.tierlist.api.ItemApi
import com.ulyup.tierlist.data.tierlist.mapper.toDomain
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.domain.tierlist.repository.ItemRepository
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.MoveItemRequest
import com.ulyup.tierlist.model.Tier

class ItemRepositoryImpl(
    private val itemApi: ItemApi,
) : ItemRepository {

    override suspend fun create(tierlistId: Int, imageUrl: String): Item =
        itemApi.create(tierlistId, CreateItemRequest(imageUrl = imageUrl)).toDomain()

    override suspend fun delete(tierlistId: Int, itemId: Int) {
        itemApi.delete(tierlistId, itemId)
    }

    override suspend fun move(tierlistId: Int, itemId: Int, tier: Tier?, position: Int): Item =
        itemApi.move(tierlistId, itemId, MoveItemRequest(tier = tier, position = position)).toDomain()
}
