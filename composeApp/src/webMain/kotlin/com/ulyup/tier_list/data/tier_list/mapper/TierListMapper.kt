package com.ulyup.tier_list.data.tier_list.mapper

import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.model.TierListDetail
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto

fun TierListDto.toDomain(): TierList = TierList(
    id = id,
    ownerId = userId,
    title = title,
    isPublic = isPublic,
    createdAt = createdAt.toEpochMilliseconds(),
)

fun TierListDetailDto.toDomain(): TierListDetail = TierListDetail(
    tierList = TierList(
        id = id,
        ownerId = userId,
        title = title,
        isPublic = isPublic,
        createdAt = createdAt.toEpochMilliseconds(),
    ),
    items = items.map { it.toDomain() },
)

