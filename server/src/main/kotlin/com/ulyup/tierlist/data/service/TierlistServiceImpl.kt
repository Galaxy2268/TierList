package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.domain.model.Tierlist
import com.ulyup.tierlist.domain.repository.ItemRepository
import com.ulyup.tierlist.domain.repository.TierlistRepository
import com.ulyup.tierlist.domain.repository.UserRepository
import com.ulyup.tierlist.domain.service.TierlistService
import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.TierlistDto
import com.ulyup.tierlist.dto.UpdateTierlistRequest
import com.ulyup.tierlist.dto.UpdateVisibilityRequest
import com.ulyup.tierlist.model.UserRole
import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.utils.CapReachedException
import com.ulyup.tierlist.utils.findOrThrow
import com.ulyup.tierlist.utils.requireOwnership

class TierlistServiceImpl(
    private val tierlistRepo: TierlistRepository,
    private val userRepo: UserRepository,
    private val itemRepo: ItemRepository,
) : TierlistService {

    override suspend fun getPublicFeed(): List<TierlistDto> =
        tierlistRepo.listPublic().map { tierlist -> tierlist.enrich() }

    override suspend fun getTierlist(session: UserSession?, id: Int): TierlistDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        if (!tierlist.isPublic && (session == null || session.userId != tierlist.userId)) {
            throw NoSuchElementException("Tierlist not found")
        }
        return tierlist.enrich()
    }

    override suspend fun getUserTierlists(session: UserSession): List<TierlistDto> =
        tierlistRepo.findByUserId(session.userId).map { tierlist ->
            tierlist.enrich(knownUsername = session.username)
        }

    override suspend fun createTierlist(session: UserSession, request: CreateTierlistRequest): TierlistDto {
        if (session.role == UserRole.USER && tierlistRepo.countByUser(session.userId) >= 5) {
            throw CapReachedException("Tierlist limit of 5 reached. Upgrade to Premium for unlimited lists.")
        }
        val tierlist = tierlistRepo.create(session.userId, request.title, request.isPublic)
        return tierlist.enrich(knownUsername = session.username)
    }

    override suspend fun updateTierlist(session: UserSession, id: Int, request: UpdateTierlistRequest): TierlistDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        session.requireOwnership(tierlist)
        val updated = tierlistRepo.update(id, request.title) ?: throw NoSuchElementException("Tierlist not found")
        return updated.enrich(knownUsername = session.username)
    }

    override suspend fun setVisibility(session: UserSession, id: Int, request: UpdateVisibilityRequest): TierlistDto {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        session.requireOwnership(tierlist)
        val updated = tierlistRepo.setVisibility(id, request.isPublic) ?: throw NoSuchElementException("Tierlist not found")
        return updated.enrich(knownUsername = session.username)
    }

    override suspend fun deleteTierlist(session: UserSession, id: Int) {
        val tierlist = findOrThrow("Tierlist") { tierlistRepo.findById(id) }
        session.requireOwnership(tierlist)
        if (!tierlistRepo.delete(id)) throw NoSuchElementException("Tierlist not found")
    }

    private suspend fun Tierlist.enrich(knownUsername: String? = null): TierlistDto {
        val ownerUsername = knownUsername ?: userRepo.findById(userId)?.username ?: "Unknown"
        return toDto(ownerUsername, itemRepo.findByTierlistId(id).size)
    }
}