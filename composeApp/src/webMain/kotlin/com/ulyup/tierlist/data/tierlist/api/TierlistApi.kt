package com.ulyup.tierlist.data.tierlist.api

import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.TierlistDetailDto
import com.ulyup.tierlist.dto.TierlistDto

interface TierlistApi {
    suspend fun getPublicTierlists(): List<TierlistDto>
    suspend fun getUserTierlists(): List<TierlistDto>
    suspend fun create(request: CreateTierlistRequest): TierlistDto
    suspend fun getDetail(id: Int): TierlistDetailDto
}
