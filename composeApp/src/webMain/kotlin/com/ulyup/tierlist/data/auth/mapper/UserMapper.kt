package com.ulyup.tierlist.data.auth.mapper

import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.dto.UserDto

fun UserDto.toDomain(): User = User(
    id = id,
    username = username,
    email = email,
    role = role,
)