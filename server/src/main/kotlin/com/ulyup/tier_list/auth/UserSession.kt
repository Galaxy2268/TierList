package com.ulyup.tier_list.auth

import com.ulyup.tier_list.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Int,
    val username: String,
    val role: UserRole,
)