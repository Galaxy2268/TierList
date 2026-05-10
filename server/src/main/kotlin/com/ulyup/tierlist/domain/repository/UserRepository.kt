package com.ulyup.tierlist.domain.repository

import com.ulyup.tierlist.domain.model.User
import com.ulyup.tierlist.model.UserRole

interface UserRepository {
    suspend fun create(username: String, email: String, passwordHash: String): User
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun findByUsernameOrEmail(value: String): User?
    suspend fun findById(id: Int): User?
    suspend fun setRole(id: Int, role: UserRole): User?
}