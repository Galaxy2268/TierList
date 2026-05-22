package com.ulyup.tierlist.data.tierlist.mapper

import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.dto.ItemDto
import kotlinx.browser.window

fun ItemDto.toDomain(): Item = Item(
    id = id,
    tierlistId = tierlistId,
    imageUrl = window.location.origin + imageUrl,
    tier = tier,
    position = position,
)
