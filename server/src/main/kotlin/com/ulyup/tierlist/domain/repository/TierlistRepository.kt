package com.ulyup.tierlist.domain.repository

import com.ulyup.tierlist.domain.model.Tierlist

interface TierlistRepository {
    suspend fun create(userId: Int, title: String, isPublic: Boolean): Tierlist
    suspend fun findById(id: Int): Tierlist?
    suspend fun findByUserId(userId: Int): List<Tierlist>
    suspend fun listPublic(limit: Int = 50, offset: Int = 0): List<Tierlist>
    suspend fun update(id: Int, userId: Int, title: String): Tierlist?
    suspend fun setVisibility(id: Int, userId: Int, isPublic: Boolean): Tierlist?
    suspend fun delete(id: Int, userId: Int): Boolean
    suspend fun countByUser(userId: Int): Long
}
