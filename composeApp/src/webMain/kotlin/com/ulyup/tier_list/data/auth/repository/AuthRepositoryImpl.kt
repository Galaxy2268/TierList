package com.ulyup.tier_list.data.auth.repository

import com.ulyup.tier_list.data.auth.api.AuthApi
import com.ulyup.tier_list.data.auth.mapper.toDomain
import com.ulyup.tier_list.data.session.SessionManager
import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.domain.auth.repository.AuthRepository
import com.ulyup.tier_list.dto.LoginRequest
import com.ulyup.tier_list.dto.RegisterRequest

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override suspend fun login(usernameOrEmail: String, password: String): User {
        val user = authApi.login(LoginRequest(usernameOrEmail = usernameOrEmail, password = password)).toDomain()
        sessionManager.signIn(user)
        return user
    }

    override suspend fun register(username: String, email: String, password: String): User {
        val user = authApi.register(RegisterRequest(username = username, email = email, password = password)).toDomain()
        sessionManager.signIn(user)
        return user
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } finally {
            sessionManager.signOut()
        }
    }
}
