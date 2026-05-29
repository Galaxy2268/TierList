package com.ulyup.tier_list.data.tier_list.repository

import com.ulyup.tier_list.data.tier_list.api.TierListApi
import com.ulyup.tier_list.data.tier_list.mapper.toDomain
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.model.TierListDetail
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository
import com.ulyup.tier_list.dto.CreateTierListRequest
import com.ulyup.tier_list.dto.UpdateTierListRequest
import com.ulyup.tier_list.dto.UpdateVisibilityRequest

class TierListRepositoryImpl(
    private val tierListApi: TierListApi,
) : TierListRepository {

    override suspend fun getPublicTierLists(): List<TierList> =
        tierListApi.getPublicTierLists().map { it.toDomain() }

    override suspend fun getUserTierLists(): List<TierList> =
        tierListApi.getUserTierLists().map { it.toDomain() }

    override suspend fun create(title: String, isPublic: Boolean): TierList =
        tierListApi.create(CreateTierListRequest(title = title, isPublic = isPublic)).toDomain()

    override suspend fun getDetail(id: Int): TierListDetail =
        tierListApi.getDetail(id).toDomain()

    override suspend fun update(id: Int, title: String): TierList =
        tierListApi.update(id, UpdateTierListRequest(title = title)).toDomain()

    override suspend fun setVisibility(id: Int, isPublic: Boolean): TierList =
        tierListApi.setVisibility(id, UpdateVisibilityRequest(isPublic = isPublic)).toDomain()

    override suspend fun setFavourite(id: Int, favourite: Boolean) {
        tierListApi.setFavourite(id, favourite)
    }

    override suspend fun delete(id: Int) {
        tierListApi.delete(id)
    }
}
