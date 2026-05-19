package com.ulyup.tierlist.data.auth.repository

import com.ulyup.tierlist.data.auth.api.AuthApi
import com.ulyup.tierlist.data.auth.mapper.toDomain
import com.ulyup.tierlist.data.session.SessionManager
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.auth.repository.AuthRepository
import com.ulyup.tierlist.dto.LoginRequest
import com.ulyup.tierlist.dto.RegisterRequest

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override suspend fun login(usernameOrEmail: String, password: String): User {
        val userDto = authApi.login(LoginRequest(usernameOrEmail = usernameOrEmail, password = password))
        sessionManager.authorize()
        return userDto.toDomain()
    }

    override suspend fun register(username: String, email: String, password: String): User {
        val userDto = authApi.register(RegisterRequest(username = username, email = email, password = password))
        sessionManager.authorize()
        return userDto.toDomain()
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } finally {
            sessionManager.unauthorize()
        }
    }
}