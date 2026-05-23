package com.ulyup.tier_list.data.auth.api

import com.ulyup.tier_list.dto.LoginRequest
import com.ulyup.tier_list.dto.RegisterRequest
import com.ulyup.tier_list.dto.UserDto

interface AuthApi {
    suspend fun login(request: LoginRequest): UserDto
    suspend fun register(request: RegisterRequest): UserDto
    suspend fun logout()
}
