package com.ulyup.tier_list.dto

import com.ulyup.tier_list.model.UserRole
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

@Serializable
data class LoginRequest(
    val usernameOrEmail: String,
    val password: String,
)

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val role: UserRole,
    val createdAt: Instant,
)
