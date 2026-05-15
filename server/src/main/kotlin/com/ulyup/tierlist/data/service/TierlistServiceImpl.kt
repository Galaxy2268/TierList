package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.data.mapper.toDetailDto
import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.domain.repository.ItemRepository
import com.ulyup.tierlist.domain.repository.TierlistRepository
import com.ulyup.tierlist.domain.service.TierlistService
import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.TierlistDetailDto
import com.ulyup.tierlist.dto.TierlistDto
import com.ulyup.tierlist.dto.UpdateTierlistRequest
import com.ulyup.tierlist.dto.UpdateVisibilityRequest
import com.ulyup.tierlist.model.UserRole
import com.ulyup.tierlist.utils.CapReachedException
import com.ulyup.tierlist.utils.NotFoundException
import com.ulyup.tierlist.utils.findOrThrow

class TierlistServiceImpl(
    private val tierlistRepo: TierlistRepository,
    private val itemRepo: ItemRepository,
) : TierlistService {

    override suspend fun getPublicFeed(): List<TierlistDto> =
        tierlistRepo.listPublic().map { it.toDto() }

    override suspend fun getTierlist(caller: Caller?, id: Int): TierlistDetailDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        if (!tierlist.isPublic && (caller == null || caller.userId != tierlist.userId)) {
            throw NotFoundException("Tierlist not found")
        }
        val items = itemRepo.findByTierlistId(id).map { it.toDto() }
        return tierlist.toDetailDto(items)
    }

    override suspend fun getUserTierlists(caller: Caller): List<TierlistDto> =
        tierlistRepo.findByUserId(caller.userId).map { it.toDto() }

    override suspend fun createTierlist(caller: Caller, request: CreateTierlistRequest): TierlistDto {
        if (caller.role == UserRole.USER && tierlistRepo.countByUser(caller.userId) >= 5) {
            throw CapReachedException("Tierlist limit of 5 reached. Upgrade to Premium for unlimited lists.")
        }
        return tierlistRepo.create(caller.userId, request.title, request.isPublic).toDto()
    }

    override suspend fun updateTierlist(caller: Caller, id: Int, request: UpdateTierlistRequest): TierlistDto =
        tierlistRepo.update(id, caller.userId, request.title)?.toDto()
            ?: throw NotFoundException("Tierlist not found")

    override suspend fun setVisibility(caller: Caller, id: Int, request: UpdateVisibilityRequest): TierlistDto =
        tierlistRepo.setVisibility(id, caller.userId, request.isPublic)?.toDto()
            ?: throw NotFoundException("Tierlist not found")

    override suspend fun deleteTierlist(caller: Caller, id: Int) {
        if (!tierlistRepo.delete(id, caller.userId)) throw NotFoundException("Tierlist not found")
    }
}
