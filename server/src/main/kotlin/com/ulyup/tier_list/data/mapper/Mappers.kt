package com.ulyup.tier_list.data.mapper

import com.ulyup.tier_list.domain.model.TierList
import com.ulyup.tier_list.domain.model.TierListItem
import com.ulyup.tier_list.domain.model.User
import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto
import com.ulyup.tier_list.dto.UserDto

fun User.toDto() = UserDto(id, username, email, role, createdAt)

fun TierList.toDto() =
    TierListDto(id, userId, title, isPublic, createdAt)

fun TierList.toDetailDto(items: List<TierListItem>) =
    TierListDetailDto(id, userId, title, isPublic, createdAt, items.map { it.toDto() })

fun TierListItem.toDto() = ItemDto(id, tierListId, imageUrl, tier, position)