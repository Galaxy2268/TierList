package com.ulyup.tier_list.domain.service

import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.domain.storage.ImageUpload
import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.MoveItemRequest
import com.ulyup.tier_list.dto.UpdateItemRequest

interface ItemService {
    suspend fun getItems(caller: Caller?, tierListId: Int): List<ItemDto>
    suspend fun createItem(caller: Caller, tierListId: Int, upload: ImageUpload): ItemDto
    suspend fun updateItem(caller: Caller, tierListId: Int, itemId: Int, request: UpdateItemRequest): ItemDto
    suspend fun moveItem(caller: Caller, tierListId: Int, itemId: Int, request: MoveItemRequest): ItemDto
    suspend fun deleteItem(caller: Caller, tierListId: Int, itemId: Int)
}
