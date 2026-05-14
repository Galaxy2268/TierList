package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.data.mapper.toDetailDto
import com.ulyup.tierlist.data.mapper.toDto
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
import com.ulyup.tierlist.utils.findOrThrow
import com.ulyup.tierlist.utils.requireOwnership

class TierlistServiceImpl(
    private val tierlistRepo: TierlistRepository,
    private val itemRepo: ItemRepository,
) : TierlistService {

    override suspend fun getPublicFeed(): List<TierlistDto> =
        tierlistRepo.listPublic().map { it.toDto() }

    override suspend fun getTierlist(session: UserSession?, id: Int): TierlistDetailDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        if (!tierlist.isPublic && (session == null || session.userId != tierlist.userId)) {
            throw NoSuchElementException("Tierlist not found")
        }
        val items = itemRepo.findByTierlistId(id).map { it.toDto() }
        return tierlist.toDetailDto(items)
    }

    override suspend fun getUserTierlists(session: UserSession): List<TierlistDto> =
        tierlistRepo.findByUserId(session.userId).map { it.toDto() }

    override suspend fun createTierlist(session: UserSession, request: CreateTierlistRequest): TierlistDto {
        if (session.role == UserRole.USER && tierlistRepo.countByUser(session.userId) >= 5) {
            throw CapReachedException("Tierlist limit of 5 reached. Upgrade to Premium for unlimited lists.")
        }
        return tierlistRepo.create(session.userId, request.title, request.isPublic).toDto()
    }

    override suspend fun updateTierlist(session: UserSession, id: Int, request: UpdateTierlistRequest): TierlistDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        session.requireOwnership(tierlist)
        val updated = tierlistRepo.update(id, request.title) ?: throw NoSuchElementException("Tierlist not found")
        return updated.toDto()
    }

    override suspend fun setVisibility(session: UserSession, id: Int, request: UpdateVisibilityRequest): TierlistDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        session.requireOwnership(tierlist)
        val updated = tierlistRepo.setVisibility(id, request.isPublic) ?: throw NoSuchElementException("Tierlist not found")
        return updated.toDto()
    }

    override suspend fun deleteTierlist(session: UserSession, id: Int) {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        session.requireOwnership(tierlist)
        if (!tierlistRepo.delete(id)) throw NoSuchElementException("Tierlist not found")
    }
}