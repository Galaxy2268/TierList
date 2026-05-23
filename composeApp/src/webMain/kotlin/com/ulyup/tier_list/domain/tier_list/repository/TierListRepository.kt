package com.ulyup.tier_list.domain.tier_list.repository

import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.model.TierListDetail

interface TierListRepository {
    suspend fun getPublicTierLists(): List<TierList>
    suspend fun getUserTierLists(): List<TierList>
    suspend fun create(title: String, isPublic: Boolean): TierList
    suspend fun getDetail(id: Int): TierListDetail
}
