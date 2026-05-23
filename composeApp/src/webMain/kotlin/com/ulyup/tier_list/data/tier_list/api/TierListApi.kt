package com.ulyup.tier_list.data.tier_list.api

import com.ulyup.tier_list.dto.CreateTierListRequest
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto

interface TierListApi {
    suspend fun getPublicTierLists(): List<TierListDto>
    suspend fun getUserTierLists(): List<TierListDto>
    suspend fun create(request: CreateTierListRequest): TierListDto
    suspend fun getDetail(id: Int): TierListDetailDto
}
