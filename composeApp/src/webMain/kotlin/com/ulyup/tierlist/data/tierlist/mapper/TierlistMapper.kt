package com.ulyup.tierlist.data.tierlist.mapper

import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.dto.TierlistDto

fun TierlistDto.toDomain(): Tierlist = Tierlist(
    id = id,
    ownerId = userId,
    title = title,
    isPublic = isPublic,
    createdAt = createdAt.toEpochMilliseconds(),
)
