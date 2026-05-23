package com.ulyup.tier_list.domain.service

import com.ulyup.tier_list.dto.UserDto

interface AuthService {
    suspend fun register(username: String, email: String, password: String): UserDto
    suspend fun login(usernameOrEmail: String, password: String): UserDto
}
