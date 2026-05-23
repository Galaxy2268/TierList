package com.ulyup.tier_list.data.auth.mapper

import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.dto.UserDto

fun UserDto.toDomain(): User = User(
    id = id,
    username = username,
    email = email,
    role = role,
)