package com.ulyup.tier_list.data.service

import com.ulyup.tier_list.data.mapper.toDto
import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.domain.repository.ItemRepository
import com.ulyup.tier_list.domain.repository.TierListRepository
import com.ulyup.tier_list.domain.service.ItemService
import com.ulyup.tier_list.domain.storage.ImageStorage
import com.ulyup.tier_list.domain.storage.ImageUpload
import com.ulyup.tier_list.domain.storage.IncomingImage
import com.ulyup.tier_list.dto.CreateItemsBatchResponse
import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.MoveItemRequest
import com.ulyup.tier_list.dto.UpdateItemRequest
import com.ulyup.tier_list.utils.BadRequestException
import com.ulyup.tier_list.utils.MAX_IMAGE_BYTES
import com.ulyup.tier_list.utils.NotFoundException
import com.ulyup.tier_list.utils.findOrThrow

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

    override suspend fun createItems(
        caller: Caller,
        tierListId: Int,
        images: List<IncomingImage>,
    ): CreateItemsBatchResponse {
        val created = mutableListOf<ItemDto>()
        val failedFilenames = mutableListOf<String>()
        for (image in images) {
            val upload = image.toValidUploadOrNull()
            if (upload == null) {
                failedFilenames += image.filename
                continue
            }
            val url = imageStorage.save(upload)
            val item = itemRepo.create(tierListId, caller.userId, url)
                ?: run {
                    imageStorage.delete(url)
                    throw NotFoundException("TierList not found")
                }
            created += item.toDto()
        }
        return CreateItemsBatchResponse(created, failedFilenames)
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

    override suspend fun clearItems(caller: Caller, tierListId: Int) {
        val deleted = itemRepo.deleteByTierListId(tierListId, caller.userId)
            ?: throw NotFoundException("TierList not found")
        deleted.forEach { imageStorage.delete(it.imageUrl) }
    }

    private fun IncomingImage.toValidUploadOrNull(): ImageUpload? {
        val contentType = contentType ?: return null
        if (contentType.contentSubtype.lowercase() !in ALLOWED_IMAGE_SUBTYPES) return null
        if (bytes.size > MAX_IMAGE_BYTES) return null
        return ImageUpload(bytes, contentType)
    }

    companion object {
        // "jpg" is non-standard but some browsers/clients send it; accept both spellings of JPEG.
        private val ALLOWED_IMAGE_SUBTYPES = setOf("jpeg", "jpg", "png", "webp")
    }
}
