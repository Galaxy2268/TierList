package com.ulyup.tierlist.data.user.api

import com.ulyup.tierlist.dto.UserDto

interface UserApi {
    suspend fun me(): UserDto
    suspend fun upgradePremium(): UserDto
}
