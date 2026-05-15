package com.ulyup.tierlist.domain.service

import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.dto.UserDto

interface UserService {
    suspend fun getCurrentUser(caller: Caller): UserDto
    suspend fun upgradeToPremium(caller: Caller): UserDto
}