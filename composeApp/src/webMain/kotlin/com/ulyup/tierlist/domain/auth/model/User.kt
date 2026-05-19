package com.ulyup.tierlist.domain.auth.model

import com.ulyup.tierlist.model.UserRole

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: UserRole,
)