package com.ulyup.tier_list.data.tier_list.api

import com.ulyup.tier_list.dto.CreateTierListRequest
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto
import com.ulyup.tier_list.dto.UpdateTierListRequest
import com.ulyup.tier_list.dto.UpdateVisibilityRequest

interface TierListApi {
    suspend fun getPublicTierLists(): List<TierListDto>
    suspend fun getUserTierLists(): List<TierListDto>
    suspend fun create(request: CreateTierListRequest): TierListDto
    suspend fun copy(id: Int): TierListDto
    suspend fun getDetail(id: Int): TierListDetailDto
    suspend fun update(id: Int, request: UpdateTierListRequest): TierListDto
    suspend fun setVisibility(id: Int, request: UpdateVisibilityRequest): TierListDto
    suspend fun setFavourite(id: Int, favourite: Boolean)
    suspend fun delete(id: Int)
}
