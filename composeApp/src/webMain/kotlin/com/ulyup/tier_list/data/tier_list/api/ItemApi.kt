package com.ulyup.tier_list.data.tier_list.api

import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.MoveItemRequest

interface ItemApi {
    suspend fun create(tierListId: Int, bytes: ByteArray, filename: String): ItemDto
    suspend fun delete(tierListId: Int, itemId: Int)
    suspend fun move(tierListId: Int, itemId: Int, request: MoveItemRequest): ItemDto
}
