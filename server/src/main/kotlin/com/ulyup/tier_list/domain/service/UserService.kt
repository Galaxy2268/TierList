package com.ulyup.tier_list.domain.service

import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.dto.UserDto

interface UserService {
    suspend fun getCurrentUser(caller: Caller): UserDto
    suspend fun upgradeToPremium(caller: Caller): UserDto
}