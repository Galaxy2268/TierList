package com.ulyup.tierlist.data.tierlist.api

import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.MoveItemRequest

interface ItemApi {
    suspend fun create(tierlistId: Int, bytes: ByteArray, filename: String): ItemDto
    suspend fun delete(tierlistId: Int, itemId: Int)
    suspend fun move(tierlistId: Int, itemId: Int, request: MoveItemRequest): ItemDto
}
