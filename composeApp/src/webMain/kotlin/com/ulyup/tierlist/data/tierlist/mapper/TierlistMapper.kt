package com.ulyup.tierlist.data.tierlist.mapper

import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.domain.tierlist.model.TierlistDetail
import com.ulyup.tierlist.dto.TierlistDetailDto
import com.ulyup.tierlist.dto.TierlistDto

fun TierlistDto.toDomain(): Tierlist = Tierlist(
    id = id,
    ownerId = userId,
    title = title,
    isPublic = isPublic,
    createdAt = createdAt.toEpochMilliseconds(),
)

fun TierlistDetailDto.toDomain(): TierlistDetail = TierlistDetail(
    tierlist = Tierlist(
        id = id,
        ownerId = userId,
        title = title,
        isPublic = isPublic,
        createdAt = createdAt.toEpochMilliseconds(),
    ),
    items = items.map { it.toDomain() },
)

