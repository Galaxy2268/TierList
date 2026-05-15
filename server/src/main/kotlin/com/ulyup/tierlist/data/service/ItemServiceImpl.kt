package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.domain.repository.ItemRepository
import com.ulyup.tierlist.domain.repository.TierlistRepository
import com.ulyup.tierlist.domain.service.ItemService
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.MoveItemRequest
import com.ulyup.tierlist.dto.UpdateItemRequest
import com.ulyup.tierlist.utils.BadRequestException
import com.ulyup.tierlist.utils.NotFoundException
import com.ulyup.tierlist.utils.findOrThrow

class ItemServiceImpl(
    private val itemRepo: ItemRepository,
    private val tierlistRepo: TierlistRepository,
) : ItemService {

    override suspend fun getItems(caller: Caller?, tierlistId: Int): List<ItemDto> {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(tierlistId) }
        if (!tierlist.isPublic && (caller == null || caller.userId != tierlist.userId)) {
            throw NotFoundException("Tierlist not found")
        }
        return itemRepo.findByTierlistId(tierlistId).map { it.toDto() }
    }

    override suspend fun createItem(caller: Caller, tierlistId: Int, request: CreateItemRequest): ItemDto =
        itemRepo.create(tierlistId, caller.userId, request.imageUrl)?.toDto()
            ?: throw NotFoundException("Tierlist not found")

    override suspend fun updateItem(caller: Caller, tierlistId: Int, itemId: Int, request: UpdateItemRequest): ItemDto =
        itemRepo.update(itemId, tierlistId, caller.userId, request.imageUrl)?.toDto()
            ?: throw NotFoundException("Item not found")

    override suspend fun moveItem(caller: Caller, tierlistId: Int, itemId: Int, request: MoveItemRequest): ItemDto {
        if (request.position < 0) throw BadRequestException("position must be >= 0")
        return itemRepo.move(itemId, tierlistId, caller.userId, request.tier, request.position)?.toDto()
            ?: throw NotFoundException("Item not found")
    }

    override suspend fun deleteItem(caller: Caller, tierlistId: Int, itemId: Int) {
        if (!itemRepo.delete(itemId, tierlistId, caller.userId)) throw NotFoundException("Item not found")
    }
}
