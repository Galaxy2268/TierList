package com.ulyup.tier_list.data.tier_list.api

import com.ulyup.tier_list.domain.tier_list.model.ItemImage
import com.ulyup.tier_list.dto.CreateItemsBatchResponse
import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.MoveItemRequest

interface ItemApi {
    suspend fun createBatch(tierListId: Int, images: List<ItemImage>): CreateItemsBatchResponse
    suspend fun delete(tierListId: Int, itemId: Int)
    suspend fun clear(tierListId: Int)
    suspend fun move(tierListId: Int, itemId: Int, request: MoveItemRequest): ItemDto
}
