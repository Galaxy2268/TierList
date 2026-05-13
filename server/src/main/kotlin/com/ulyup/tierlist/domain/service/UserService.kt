package com.ulyup.tierlist.domain.service

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.dto.UserDto

interface UserService {
    suspend fun getCurrentUser(session: UserSession): UserDto
    suspend fun upgradeToPremium(session: UserSession): UserDto
}