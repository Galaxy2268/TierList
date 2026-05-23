package com.ulyup.tier_list.domain.auth.model

import com.ulyup.tier_list.model.UserRole

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: UserRole,
)