package com.ulyup.tierlist.domain.service

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.UpdateItemRequest

interface ItemService {
    suspend fun getItems(session: UserSession?, tierlistId: Int): List<ItemDto>
    suspend fun createItem(session: UserSession, tierlistId: Int, request: CreateItemRequest): ItemDto
    suspend fun updateItem(session: UserSession, tierlistId: Int, itemId: Int, request: UpdateItemRequest): ItemDto
    suspend fun deleteItem(session: UserSession, tierlistId: Int, itemId: Int)
}