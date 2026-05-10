package com.ulyup.tierlist.auth

import com.ulyup.tierlist.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Int,
    val username: String,
    val role: UserRole,
)