package com.ulyup.tier_list.domain.tier_list.repository

import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.model.TierListDetail

interface TierListRepository {
    suspend fun getPublicTierLists(): List<TierList>
    suspend fun getUserTierLists(): List<TierList>
    suspend fun create(title: String, isPublic: Boolean): TierList
    suspend fun copy(id: Int): TierList
    suspend fun getDetail(id: Int): TierListDetail
    suspend fun update(id: Int, title: String): TierList
    suspend fun setVisibility(id: Int, isPublic: Boolean): TierList
    suspend fun setFavourite(id: Int, favourite: Boolean)
    suspend fun delete(id: Int)
}
