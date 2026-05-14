package com.ulyup.tierlist.data.mapper

import com.ulyup.tierlist.domain.model.Tierlist
import com.ulyup.tierlist.domain.model.TierlistItem
import com.ulyup.tierlist.domain.model.User
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.TierlistDetailDto
import com.ulyup.tierlist.dto.TierlistDto
import com.ulyup.tierlist.dto.UserDto

fun User.toDto() = UserDto(id, username, email, role, createdAt)

fun Tierlist.toDto() =
    TierlistDto(id, userId, title, isPublic, createdAt)

fun Tierlist.toDetailDto(items: List<ItemDto>) =
    TierlistDetailDto(id, userId, title, isPublic, createdAt, items)

fun TierlistItem.toDto() = ItemDto(id, tierlistId, imageUrl, tier, position)