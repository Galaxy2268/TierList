package com.ulyup.tier_list.domain.repository

import com.ulyup.tier_list.domain.model.User
import com.ulyup.tier_list.model.UserRole

interface UserRepository {
    suspend fun create(username: String, email: String, passwordHash: String): User
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun findByUsernameOrEmail(value: String): User?
    suspend fun findById(id: Int): User?
    suspend fun setRole(id: Int, role: UserRole): User?
}