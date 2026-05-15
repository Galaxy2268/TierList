package com.ulyup.tierlist.domain.service

import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.MoveItemRequest
import com.ulyup.tierlist.dto.UpdateItemRequest

interface ItemService {
    suspend fun getItems(caller: Caller?, tierlistId: Int): List<ItemDto>
    suspend fun createItem(caller: Caller, tierlistId: Int, request: CreateItemRequest): ItemDto
    suspend fun updateItem(caller: Caller, tierlistId: Int, itemId: Int, request: UpdateItemRequest): ItemDto
    suspend fun moveItem(caller: Caller, tierlistId: Int, itemId: Int, request: MoveItemRequest): ItemDto
    suspend fun deleteItem(caller: Caller, tierlistId: Int, itemId: Int)
}