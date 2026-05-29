package com.ulyup.tier_list.data.service

import com.ulyup.tier_list.FREE_TIER_LIMIT
import com.ulyup.tier_list.data.mapper.toDetailDto
import com.ulyup.tier_list.data.mapper.toDto
import com.ulyup.tier_list.domain.model.Caller
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
        val tierList = findOrThrow("TierList") { tierListRepo.findById(id) }
        if (!tierList.isPublic && (caller == null || caller.userId != tierList.userId)) {
            throw NotFoundException("TierList not found")
        }
        val isFavourite = caller != null && favouriteRepo.isFavourite(caller.userId, id)
        return tierList.toDetailDto(itemRepo.findByTierListId(id), isFavourite)
    }

    override suspend fun getUserTierLists(caller: Caller): List<TierListDto> {
        val favouriteIds = favouriteRepo.findTierListIdsByUser(caller.userId)
        return tierListRepo.findByUserId(caller.userId).map { it.toDto(isFavourite = it.id in favouriteIds) }
    }

    override suspend fun createTierList(caller: Caller, request: CreateTierListRequest): TierListDto {
        if (caller.role == UserRole.USER && tierListRepo.countByUser(caller.userId) >= FREE_TIER_LIMIT) {
            throw CapReachedException("TierList limit of $FREE_TIER_LIMIT reached. Upgrade to Premium for unlimited lists.")
        }
        return tierListRepo.create(caller.userId, request.title, request.isPublic).toDto()
    }

    override suspend fun updateTierList(caller: Caller, id: Int, request: UpdateTierListRequest): TierListDto =
        tierListRepo.update(id, caller.userId, request.title)?.toDto()
            ?: throw NotFoundException("TierList not found")

    override suspend fun setVisibility(caller: Caller, id: Int, request: UpdateVisibilityRequest): TierListDto =
        tierListRepo.setVisibility(id, caller.userId, request.isPublic)?.toDto()
            ?: throw NotFoundException("TierList not found")

    override suspend fun deleteTierList(caller: Caller, id: Int) {
        val imageUrls = itemRepo.findByTierListId(id).map { it.imageUrl }
        if (!tierListRepo.delete(id, caller.userId)) throw NotFoundException("TierList not found")
        imageUrls.forEach { imageStorage.delete(it) }
    }

    override suspend fun setFavourite(caller: Caller, id: Int, favourite: Boolean) {
        val tierList = findOrThrow("TierList") { tierListRepo.findById(id) }
        if (!tierList.isPublic && caller.userId != tierList.userId) {
            throw NotFoundException("TierList not found")
        }
        if (favourite) favouriteRepo.add(caller.userId, id) else favouriteRepo.remove(caller.userId, id)
    }
}
