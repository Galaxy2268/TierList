package com.ulyup.tierlist.data.auth.api

import com.ulyup.tierlist.dto.LoginRequest
import com.ulyup.tierlist.dto.RegisterRequest
import com.ulyup.tierlist.dto.UserDto

interface AuthApi {
    suspend fun login(request: LoginRequest): UserDto
    suspend fun register(request: RegisterRequest): UserDto
    suspend fun logout()
    suspend fun me(): UserDto
}