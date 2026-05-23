package com.ulyup.tier_list.data.tier_list.mapper

import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.dto.ItemDto
import kotlinx.browser.window

fun ItemDto.toDomain(): TierListItem = TierListItem(
    id = id,
    tierListId = tierListId,
    imageUrl = window.location.origin + imageUrl,
    tier = tier,
    position = position,
)
