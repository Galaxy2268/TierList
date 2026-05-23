package com.ulyup.tier_list.domain.service

import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.dto.CreateTierListRequest
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto
import com.ulyup.tier_list.dto.UpdateTierListRequest
import com.ulyup.tier_list.dto.UpdateVisibilityRequest

interface TierListService {
    suspend fun getPublicFeed(): List<TierListDto>
    suspend fun getTierList(caller: Caller?, id: Int): TierListDetailDto
    suspend fun getUserTierLists(caller: Caller): List<TierListDto>
    suspend fun createTierList(caller: Caller, request: CreateTierListRequest): TierListDto
    suspend fun updateTierList(caller: Caller, id: Int, request: UpdateTierListRequest): TierListDto
    suspend fun setVisibility(caller: Caller, id: Int, request: UpdateVisibilityRequest): TierListDto
    suspend fun deleteTierList(caller: Caller, id: Int)
}