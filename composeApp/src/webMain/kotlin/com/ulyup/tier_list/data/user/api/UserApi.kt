package com.ulyup.tier_list.data.user.api

import com.ulyup.tier_list.dto.UserDto

interface UserApi {
    suspend fun me(): UserDto
    suspend fun upgradePremium(): UserDto
}
