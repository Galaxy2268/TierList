package com.ulyup.tierlist.data.tierlist.api

import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.TierlistDto

interface TierlistApi {
    suspend fun getPublicTierlists(): List<TierlistDto>
    suspend fun getUserTierlists(): List<TierlistDto>
    suspend fun create(request: CreateTierlistRequest): TierlistDto
}
