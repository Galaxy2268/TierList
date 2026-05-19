package com.ulyup.tierlist.domain.tierlist.repository

import com.ulyup.tierlist.domain.tierlist.model.Tierlist

interface TierlistRepository {
    suspend fun getPublicTierlists(): List<Tierlist>
    suspend fun getUserTierlists(): List<Tierlist>
    suspend fun create(title: String, isPublic: Boolean): Tierlist
}
