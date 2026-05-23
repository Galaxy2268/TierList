package com.ulyup.tier_list.data.service

import com.ulyup.tier_list.data.mapper.toDto
import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.domain.repository.ItemRepository
import com.ulyup.tier_list.domain.repository.TierListRepository
import com.ulyup.tier_list.domain.service.ItemService
import com.ulyup.tier_list.domain.storage.ImageStorage
import com.ulyup.tier_list.domain.storage.ImageUpload
import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.MoveItemRequest
import com.ulyup.tier_list.dto.UpdateItemRequest
import com.ulyup.tier_list.utils.BadRequestException
import com.ulyup.tier_list.utils.NotFoundException
import com.ulyup.tier_list.utils.findOrThrow
import io.ktor.http.ContentType

class ItemServiceImpl(
    private val itemRepo: ItemRepository,
    private val tierListRepo: TierListRepository,
    private val imageStorage: ImageStorage,
) : ItemService {

    override suspend fun getItems(caller: Caller?, tierListId: Int): List<ItemDto> {
        val tierList = findOrThrow("TierList") { tierListRepo.findById(tierListId) }
        if (!tierList.isPublic && (caller == null || caller.userId != tierList.userId)) {
            throw NotFoundException("TierList not found")
        }
        return itemRepo.findByTierListId(tierListId).map { it.toDto() }
    }

    override suspend fun createItem(caller: Caller, tierListId: Int, upload: ImageUpload): ItemDto {
        validateImageContentType(upload.contentType)
        val url = imageStorage.save(upload)
        return itemRepo.create(tierListId, caller.userId, url)?.toDto()
            ?: run {
                imageStorage.delete(url)
                throw NotFoundException("TierList not found")
            }
    }

    override suspend fun updateItem(caller: Caller, tierListId: Int, itemId: Int, request: UpdateItemRequest): ItemDto =
        itemRepo.update(itemId, tierListId, caller.userId, request.imageUrl)?.toDto()
            ?: throw NotFoundException("Item not found")

    override suspend fun moveItem(caller: Caller, tierListId: Int, itemId: Int, request: MoveItemRequest): ItemDto {
        if (request.position < 0) throw BadRequestException("position must be >= 0")
        return itemRepo.move(itemId, tierListId, caller.userId, request.tier, request.position)?.toDto()
            ?: throw NotFoundException("Item not found")
    }

    override suspend fun deleteItem(caller: Caller, tierListId: Int, itemId: Int) {
        val deleted = itemRepo.delete(itemId, tierListId, caller.userId)
            ?: throw NotFoundException("Item not found")
        imageStorage.delete(deleted.imageUrl)
    }

    private fun validateImageContentType(contentType: ContentType) {
        if (contentType.contentSubtype.lowercase() !in ALLOWED_IMAGE_SUBTYPES) {
            throw BadRequestException("Only JPEG, PNG, or WebP images are accepted")
        }
    }

    companion object {
        // "jpg" is non-standard but some browsers/clients send it; accept both spellings of JPEG.
        private val ALLOWED_IMAGE_SUBTYPES = setOf("jpeg", "jpg", "png", "webp")
    }
}
