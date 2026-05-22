package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.domain.repository.ItemRepository
import com.ulyup.tierlist.domain.repository.TierlistRepository
import com.ulyup.tierlist.domain.service.ItemService
import com.ulyup.tierlist.domain.storage.ImageStorage
import com.ulyup.tierlist.domain.storage.ImageUpload
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.MoveItemRequest
import com.ulyup.tierlist.dto.UpdateItemRequest
import com.ulyup.tierlist.utils.BadRequestException
import com.ulyup.tierlist.utils.NotFoundException
import com.ulyup.tierlist.utils.findOrThrow
import io.ktor.http.ContentType

class ItemServiceImpl(
    private val itemRepo: ItemRepository,
    private val tierlistRepo: TierlistRepository,
    private val imageStorage: ImageStorage,
) : ItemService {

    override suspend fun getItems(caller: Caller?, tierlistId: Int): List<ItemDto> {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(tierlistId) }
        if (!tierlist.isPublic && (caller == null || caller.userId != tierlist.userId)) {
            throw NotFoundException("Tierlist not found")
        }
        return itemRepo.findByTierlistId(tierlistId).map { it.toDto() }
    }

    override suspend fun createItem(caller: Caller, tierlistId: Int, upload: ImageUpload): ItemDto {
        validateImageContentType(upload.contentType)
        val url = imageStorage.save(upload)
        return itemRepo.create(tierlistId, caller.userId, url)?.toDto()
            ?: run {
                imageStorage.delete(url)
                throw NotFoundException("Tierlist not found")
            }
    }

    override suspend fun updateItem(caller: Caller, tierlistId: Int, itemId: Int, request: UpdateItemRequest): ItemDto =
        itemRepo.update(itemId, tierlistId, caller.userId, request.imageUrl)?.toDto()
            ?: throw NotFoundException("Item not found")

    override suspend fun moveItem(caller: Caller, tierlistId: Int, itemId: Int, request: MoveItemRequest): ItemDto {
        if (request.position < 0) throw BadRequestException("position must be >= 0")
        return itemRepo.move(itemId, tierlistId, caller.userId, request.tier, request.position)?.toDto()
            ?: throw NotFoundException("Item not found")
    }

    override suspend fun deleteItem(caller: Caller, tierlistId: Int, itemId: Int) {
        val deleted = itemRepo.delete(itemId, tierlistId, caller.userId)
            ?: throw NotFoundException("Item not found")
        imageStorage.delete(deleted.imageUrl)
    }

    private fun validateImageContentType(contentType: ContentType) {
        if (contentType.contentSubtype.lowercase() !in ALLOWED_IMAGE_SUBTYPES) {
            throw BadRequestException("Only JPEG, PNG, or WebP images are accepted")
        }
    }

    companion object {
        private val ALLOWED_IMAGE_SUBTYPES = setOf("jpeg", "jpg", "png", "webp")
    }
}
