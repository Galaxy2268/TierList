package com.ulyup.tier_list.data.mapper

import com.ulyup.tier_list.domain.model.TierList
import com.ulyup.tier_list.domain.model.TierListItem
import com.ulyup.tier_list.domain.model.User
import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto
import com.ulyup.tier_list.dto.UserDto

fun User.toDto() = UserDto(id, username, email, role, createdAt)

fun TierList.toDto(isFavourite: Boolean = false) =
    TierListDto(id, userId, title, isPublic, createdAt, isFavourite)

fun TierList.toDetailDto(items: List<TierListItem>, isFavourite: Boolean = false) =
    TierListDetailDto(id, userId, title, isPublic, createdAt, items.map { it.toDto() }, isFavourite)

fun TierListItem.toDto() = ItemDto(id, tierListId, imageUrl, tier, position)