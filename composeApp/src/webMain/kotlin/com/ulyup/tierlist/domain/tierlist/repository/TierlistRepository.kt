package com.ulyup.tierlist.domain.tierlist.repository

import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.domain.tierlist.model.TierlistDetail

interface TierlistRepository {
    suspend fun getPublicTierlists(): List<Tierlist>
    suspend fun getUserTierlists(): List<Tierlist>
    suspend fun create(title: String, isPublic: Boolean): Tierlist
    suspend fun getDetail(id: Int): TierlistDetail
}
