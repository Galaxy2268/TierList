package com.ulyup.tierlist.data.tierlist.mapper

import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.dto.ItemDto

fun ItemDto.toDomain(): Item = Item(
    id = id,
    tierlistId = tierlistId,
    imageUrl = imageUrl,
    tier = tier,
    position = position,
)
