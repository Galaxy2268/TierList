package com.ulyup.tierlist.domain.auth.repository

import com.ulyup.tierlist.domain.auth.model.User

interface AuthRepository {
    suspend fun login(usernameOrEmail: String, password: String): User
    suspend fun register(username: String, email: String, password: String): User
    suspend fun logout()
}