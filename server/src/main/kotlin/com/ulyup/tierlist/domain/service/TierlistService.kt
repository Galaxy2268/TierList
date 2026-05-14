package com.ulyup.tierlist.domain.service

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.TierlistDetailDto
import com.ulyup.tierlist.dto.TierlistDto
import com.ulyup.tierlist.dto.UpdateTierlistRequest
import com.ulyup.tierlist.dto.UpdateVisibilityRequest

interface TierlistService {
    suspend fun getPublicFeed(): List<TierlistDto>
    suspend fun getTierlist(session: UserSession?, id: Int): TierlistDetailDto
    suspend fun getUserTierlists(session: UserSession): List<TierlistDto>
    suspend fun createTierlist(session: UserSession, request: CreateTierlistRequest): TierlistDto
    suspend fun updateTierlist(session: UserSession, id: Int, request: UpdateTierlistRequest): TierlistDto
    suspend fun setVisibility(session: UserSession, id: Int, request: UpdateVisibilityRequest): TierlistDto
    suspend fun deleteTierlist(session: UserSession, id: Int)
}