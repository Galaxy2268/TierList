package com.ulyup.tier_list.data.service

import com.ulyup.tier_list.FREE_TIER_LIMIT
import com.ulyup.tier_list.TIER_LIST_TITLE_MAX_LENGTH
import com.ulyup.tier_list.data.mapper.toDetailDto
import com.ulyup.tier_list.data.mapper.toDto
import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.domain.model.TierList
import com.ulyup.tier_list.domain.repository.FavouriteRepository
import com.ulyup.tier_list.domain.repository.ItemRepository
import com.ulyup.tier_list.domain.repository.TierListRepository
import com.ulyup.tier_list.domain.service.TierListService
import com.ulyup.tier_list.domain.storage.ImageStorage
import com.ulyup.tier_list.dto.CreateTierListRequest
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto
import com.ulyup.tier_list.dto.UpdateTierListRequest
import com.ulyup.tier_list.dto.UpdateVisibilityRequest
import com.ulyup.tier_list.model.UserRole
import com.ulyup.tier_list.utils.BadRequestException
import com.ulyup.tier_list.utils.CapReachedException
import com.ulyup.tier_list.utils.NotFoundException
import com.ulyup.tier_list.utils.findOrThrow

class TierListServiceImpl(
    private val tierListRepo: TierListRepository,
    private val itemRepo: ItemRepository,
    private val favouriteRepo: FavouriteRepository,
    private val imageStorage: ImageStorage,
) : TierListService {

    override suspend fun getPublicFeed(caller: Caller?): List<TierListDto> {
        val favouriteIds = caller?.let { favouriteRepo.findTierListIdsByUser(it.userId) } ?: emptySet()
        return tierListRepo.listPublic().map { it.toDto(isFavourite = it.id in favouriteIds) }
    }

    override suspend fun getTierList(caller: Caller?, id: Int): TierListDetailDto {
        val tierList = requireVisibleTierList(caller, id)
        val isFavourite = caller != null && favouriteRepo.isFavourite(caller.userId, id)
        return tierList.toDetailDto(itemRepo.findByTierListId(id), isFavourite)
    }

    override suspend fun getUserTierLists(caller: Caller): List<TierListDto> {
        val favouriteIds = favouriteRepo.findTierListIdsByUser(caller.userId)
        return tierListRepo.findByUserId(caller.userId).map { it.toDto(isFavourite = it.id in favouriteIds) }
    }

    override suspend fun createTierList(caller: Caller, request: CreateTierListRequest): TierListDto {
        requireValidTitle(request.title)
        requireUnderCap(caller)
        return tierListRepo.create(caller.userId, request.title, request.isPublic).toDto()
    }

    override suspend fun copyTierList(caller: Caller, sourceId: Int): TierListDto {
        val source = requireVisibleTierList(caller, sourceId)
        requireUnderCap(caller)

        val sourceItems = itemRepo.findByTierListId(sourceId)
        val copy = tierListRepo.create(caller.userId, source.title, isPublic = false)
        val copiedUrls = mutableListOf<String>()
        try {
            sourceItems.forEach { item ->
                val newUrl = imageStorage.copy(item.imageUrl)
                copiedUrls += newUrl
                itemRepo.insertCopy(copy.id, newUrl, item.tier, item.position)
            }
        } catch (exception: Exception) {
            // Items are copied across multiple transactions + file ops, so a mid-way
            // failure would leave a partial list. Roll it back best-effort.
            tierListRepo.delete(copy.id, caller.userId)
            copiedUrls.forEach { imageStorage.delete(it) }
            throw exception
        }
        return copy.toDto()
    }

    private fun requireValidTitle(title: String) {
        if (title.length > TIER_LIST_TITLE_MAX_LENGTH) {
            throw BadRequestException("Title must be at most $TIER_LIST_TITLE_MAX_LENGTH characters")
        }
    }

    private suspend fun requireUnderCap(caller: Caller) {
        if (caller.role == UserRole.USER && tierListRepo.countByUser(caller.userId) >= FREE_TIER_LIMIT) {
            throw CapReachedException("TierList limit of $FREE_TIER_LIMIT reached. Upgrade to Premium for unlimited lists.")
        }
    }

    private suspend fun requireVisibleTierList(caller: Caller?, id: Int): TierList {
        val tierList = findOrThrow("TierList") { tierListRepo.findById(id) }
        if (!tierList.isPublic && caller?.userId != tierList.userId) {
            throw NotFoundException("TierList not found")
        }
        return tierList
    }

    override suspend fun updateTierList(caller: Caller, id: Int, request: UpdateTierListRequest): TierListDto {
        requireValidTitle(request.title)
        return tierListRepo.update(id, caller.userId, request.title)?.toDto()
            ?: throw NotFoundException("TierList not found")
    }

    override suspend fun setVisibility(caller: Caller, id: Int, request: UpdateVisibilityRequest): TierListDto =
        tierListRepo.setVisibility(id, caller.userId, request.isPublic)?.toDto()
            ?: throw NotFoundException("TierList not found")

    override suspend fun deleteTierList(caller: Caller, id: Int) {
        val imageUrls = itemRepo.findByTierListId(id).map { it.imageUrl }
        if (!tierListRepo.delete(id, caller.userId)) throw NotFoundException("TierList not found")
        imageUrls.forEach { imageStorage.delete(it) }
    }

    override suspend fun setFavourite(caller: Caller, id: Int, favourite: Boolean) {
        requireVisibleTierList(caller, id)
        if (favourite) favouriteRepo.add(caller.userId, id) else favouriteRepo.remove(caller.userId, id)
    }
}
