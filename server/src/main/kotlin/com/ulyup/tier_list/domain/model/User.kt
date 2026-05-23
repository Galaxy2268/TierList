package com.ulyup.tier_list.domain.model

import com.ulyup.tier_list.model.UserRole
import kotlin.time.Instant

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val createdAt: Instant,
)