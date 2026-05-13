package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.repository.ItemRepository
import com.ulyup.tierlist.domain.repository.TierlistRepository
import com.ulyup.tierlist.domain.service.ItemService
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.UpdateItemRequest
import com.ulyup.tierlist.utils.findOrThrow
import com.ulyup.tierlist.utils.requireOwnership

class ItemServiceImpl(
    private val itemRepo: ItemRepository,
    private val tierlistRepo: TierlistRepository,
) : ItemService {

    override suspend fun getItems(session: UserSession?, tierlistId: Int): List<ItemDto> {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(tierlistId) }
        if (!tierlist.isPublic && (session == null || session.userId != tierlist.userId)) {
            throw NoSuchElementException("Tierlist not found")
        }
        return itemRepo.findByTierlistId(tierlistId).map { it.toDto() }
    }

    override suspend fun createItem(session: UserSession, tierlistId: Int, request: CreateItemRequest): ItemDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(tierlistId) }
        session.requireOwnership(tierlist)
        val position = itemRepo.nextPosition(tierlistId, request.tier)
        return itemRepo.create(tierlistId, request.imageUrl, request.tier, position).toDto()
    }

    override suspend fun updateItem(session: UserSession, tierlistId: Int, itemId: Int, request: UpdateItemRequest): ItemDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(tierlistId) }
        session.requireOwnership(tierlist)
        return itemRepo.update(itemId, tierlistId, request.imageUrl)?.toDto()
            ?: throw NoSuchElementException("Item not found")
    }

    override suspend fun deleteItem(session: UserSession, tierlistId: Int, itemId: Int) {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(tierlistId) }
        session.requireOwnership(tierlist)
        if (!itemRepo.delete(itemId, tierlistId)) throw NoSuchElementException("Item not found")
    }
}