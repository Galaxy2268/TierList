package com.ulyup.tierlist.domain.service

import com.ulyup.tierlist.dto.UserDto

interface AuthService {
    suspend fun register(username: String, email: String, password: String): UserDto
    suspend fun login(usernameOrEmail: String, password: String): UserDto
    suspend fun getUser(id: Int): UserDto
}