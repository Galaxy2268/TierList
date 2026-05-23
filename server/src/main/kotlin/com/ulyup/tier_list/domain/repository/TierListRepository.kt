package com.ulyup.tier_list.domain.repository

import com.ulyup.tier_list.domain.model.TierList

interface TierListRepository {
    suspend fun create(userId: Int, title: String, isPublic: Boolean): TierList
    suspend fun findById(id: Int): TierList?
    suspend fun findByUserId(userId: Int): List<TierList>
    suspend fun listPublic(limit: Int = 50, offset: Int = 0): List<TierList>
    suspend fun update(id: Int, userId: Int, title: String): TierList?
    suspend fun setVisibility(id: Int, userId: Int, isPublic: Boolean): TierList?
    suspend fun delete(id: Int, userId: Int): Boolean
    suspend fun countByUser(userId: Int): Long
}
