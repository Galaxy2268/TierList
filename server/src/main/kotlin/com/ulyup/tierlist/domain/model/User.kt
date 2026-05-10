package com.ulyup.tierlist.domain.model

import com.ulyup.tierlist.model.UserRole
import kotlin.time.Instant

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val createdAt: Instant,
)