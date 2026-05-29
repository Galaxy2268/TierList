package com.ulyup.tier_list.data.tier_list.repository

import com.ulyup.tier_list.data.tier_list.api.ItemApi
import com.ulyup.tier_list.data.tier_list.mapper.toDomain
import com.ulyup.tier_list.domain.tier_list.model.BatchUploadResult
import com.ulyup.tier_list.domain.tier_list.model.ItemImage
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository
import com.ulyup.tier_list.dto.MoveItemRequest
import com.ulyup.tier_list.model.Tier

class ItemRepositoryImpl(
    private val itemApi: ItemApi,
) : ItemRepository {

    override suspend fun createBatch(tierListId: Int, images: List<ItemImage>): BatchUploadResult {
        val response = itemApi.createBatch(tierListId, images)
        return BatchUploadResult(
            created = response.created.map { it.toDomain() },
            failedFilenames = response.failedFilenames,
        )
    }

    override suspend fun delete(tierListId: Int, itemId: Int) {
        itemApi.delete(tierListId, itemId)
    }

    override suspend fun clear(tierListId: Int) {
        itemApi.clear(tierListId)
    }

    override suspend fun move(tierListId: Int, itemId: Int, tier: Tier?, position: Int): TierListItem =
        itemApi.move(tierListId, itemId, MoveItemRequest(tier = tier, position = position)).toDomain()
}
