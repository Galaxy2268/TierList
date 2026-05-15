package com.ulyup.tierlist.domain.service

import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.TierlistDetailDto
import com.ulyup.tierlist.dto.TierlistDto
import com.ulyup.tierlist.dto.UpdateTierlistRequest
import com.ulyup.tierlist.dto.UpdateVisibilityRequest

interface TierlistService {
    suspend fun getPublicFeed(): List<TierlistDto>
    suspend fun getTierlist(caller: Caller?, id: Int): TierlistDetailDto
    suspend fun getUserTierlists(caller: Caller): List<TierlistDto>
    suspend fun createTierlist(caller: Caller, request: CreateTierlistRequest): TierlistDto
    suspend fun updateTierlist(caller: Caller, id: Int, request: UpdateTierlistRequest): TierlistDto
    suspend fun setVisibility(caller: Caller, id: Int, request: UpdateVisibilityRequest): TierlistDto
    suspend fun deleteTierlist(caller: Caller, id: Int)
}