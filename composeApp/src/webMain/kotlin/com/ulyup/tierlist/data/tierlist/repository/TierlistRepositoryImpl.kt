package com.ulyup.tierlist.data.tierlist.repository

import com.ulyup.tierlist.data.tierlist.api.TierlistApi
import com.ulyup.tierlist.data.tierlist.mapper.toDomain
import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.domain.tierlist.model.TierlistDetail
import com.ulyup.tierlist.domain.tierlist.repository.TierlistRepository
import com.ulyup.tierlist.dto.CreateTierlistRequest

class TierlistRepositoryImpl(
    private val tierlistApi: TierlistApi,
) : TierlistRepository {

    override suspend fun getPublicTierlists(): List<Tierlist> =
        tierlistApi.getPublicTierlists().map { it.toDomain() }

    override suspend fun getUserTierlists(): List<Tierlist> =
        tierlistApi.getUserTierlists().map { it.toDomain() }

    override suspend fun create(title: String, isPublic: Boolean): Tierlist =
        tierlistApi.create(CreateTierlistRequest(title = title, isPublic = isPublic)).toDomain()

    override suspend fun getDetail(id: Int): TierlistDetail =
        tierlistApi.getDetail(id).toDomain()
}
